/**
 * 表列
 */

export const caseColumns = [
  {
    title: '用例名称',
    dataIndex: 'name',
    ellipsis: true
  },
  {
    title: '所属模块',
    dataIndex: 'module'
  },
  {
    title: '用例等级',
    dataIndex: 'rank',
    scopedSlots: { customRender: 'rank' }
  },
  {
    title: '用例描述',
    dataIndex: 'description',
    ellipsis: true
  },
  {
    title: '用例状态',
    dataIndex: 'status',
    ellipsis: true
  },
  {
    title: '操作',
    dataIndex: 'action',
    width: '20%',
    fixed: 'right',
    scopedSlots: { customRender: 'action' }
  }
]
