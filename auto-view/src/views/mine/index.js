/**
 * 表列
 */

export const caseColumns = [
  {
    title: '用例名称',
    dataIndex: 'name',
    align: 'center',
    ellipsis: true
  },
  {
    title: '所属模块',
    dataIndex: 'moduleName',
    align: 'center'
  },
  {
    title: '用例等级',
    dataIndex: 'caseRank',
    scopedSlots: { customRender: 'caseRank' },
    align: 'center'
  },
  {
    title: '业务场景描述',
    dataIndex: 'businessDesc',
    scopedSlots: { customRender: 'businessDesc' },
    align: 'center'
  },
  {
    title: '用例状态',
    dataIndex: 'status',
    scopedSlots: { customRender: 'status' },
    align: 'center'
  },
  {
    title: '操作',
    dataIndex: 'action',
    scopedSlots: { customRender: 'action' },
    align: 'center'
  }
]
