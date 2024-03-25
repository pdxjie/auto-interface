package com.pdx.controller;

import cn.bugstack.chatglm.model.*;
import cn.bugstack.chatglm.session.OpenAiSession;
import com.alibaba.fastjson.JSON;
import io.reactivex.annotations.Nullable;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @Author: IT 派同学
 * @DateTime: 2024/1/1
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/ai")
@CrossOrigin
public class ChatAnalysisController {

    @Autowired(required = false)
    private OpenAiSession openAiSession;

    @GetMapping(value = "/business")
    public SseEmitter cosplayRoleChat(@RequestParam("message") String message) {
        // 创建一个SseEmitter对象，用于与客户端进行SSE通信
        final SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        // 使用线程池异步处理聊天请求
        try {
            ChatCompletionRequest request = new ChatCompletionRequest();
            request.setModel(Model.GLM_3_5_TURBO); // chatGLM_6b_SSE、chatglm_lite、chatglm_lite_32k、chatglm_std、chatglm_pro
            request.setIncremental(false);
            request.setIsCompatible(true); // 是否对返回结果数据做兼容，24年1月发布的 GLM_3_5_TURBO、GLM_4 模型，与之前的模型在返回结果上有差异。开启 true 可以做兼容。
            // 24年1月发布的 glm-3-turbo、glm-4 支持函数、知识库、联网功能
//            request.setTools(new ArrayList<ChatCompletionRequest.Tool>() {
//                private static final long serialVersionUID = -7988151926241837899L;
//                {
//                    add(ChatCompletionRequest.Tool.builder()
//                            .type(ChatCompletionRequest.Tool.Type.web_search)
//                            .webSearch(ChatCompletionRequest.Tool.WebSearch.builder().enable(true).searchQuery("小傅哥").build())
//                            .build());
//                }
//            });
            request.setPrompt(new ArrayList<ChatCompletionRequest.Prompt>() {
                private static final long serialVersionUID = -7988151926241837899L;
                {
                    add(ChatCompletionRequest.Prompt.builder()
                            .role(Role.user.getCode())
                            .content(message)
                            .build());
                }
            });
            // 请求
            openAiSession.completions(request, new EventSourceListener() {
                @Override
                public void onEvent(EventSource eventSource, @Nullable String id, @Nullable String type, String data) {
                    ChatCompletionResponse response = JSON.parseObject(data, ChatCompletionResponse.class);
                    log.info("测试结果 onEvent：{}", response.getData());
                    try {
                        sseEmitter.send(response.getData());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    // type 消息类型，add 增量，finish 结束，error 错误，interrupted 中断
                    if (EventType.finish.getCode().equals(type)) {
                        ChatCompletionResponse.Meta meta = JSON.parseObject(response.getMeta(), ChatCompletionResponse.Meta.class);
                        log.info("[输出结束] Tokens {}", JSON.toJSONString(meta));
                    }
                }
                @Override
                public void onClosed(EventSource eventSource) {
                    log.info("对话完成");
                }
                @Override
                public void onFailure(EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
                    log.info("对话异常");
                }
            });
        } catch (Exception exception) {
            // 捕获并记录异常，然后完成SseEmitter
            log.error("抛出异常, {}", exception.getMessage());
            sseEmitter.complete();
        }
        // 返回SseEmitter对象，供客户端使用
        return sseEmitter;
    }
}
