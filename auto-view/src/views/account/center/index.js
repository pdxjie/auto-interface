export const UserColumn = [
  {
    title: '头像',
    dataIndex: 'avatar',
    key: 'avatar',
    align: 'center',
    scopedSlots: { customRender: 'avatar' }
  },
  {
    title: '邮箱',
    dataIndex: 'email',
    key: 'email',
    align: 'center',
    scopedSlots: { customRender: 'email' }
  },
  {
    title: '昵称',
    dataIndex: 'nickName',
    key: 'nickName',
    align: 'center'
  },
  {
    title: '性别',
    dataIndex: 'sex',
    key: 'sex',
    align: 'center',
    scopedSlots: { customRender: 'sex' }
  },
  {
    title: '是否在线',
    dataIndex: 'online',
    key: 'online',
    align: 'center',
    scopedSlots: { customRender: 'online' }
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    align: 'center',
    scopedSlots: { customRender: 'status' }
  },
  {
    title: '角色',
    dataIndex: 'role',
    key: 'role',
    align: 'center',
    scopedSlots: { customRender: 'role' }
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    key: 'createTime',
    align: 'center',
    scopedSlots: { customRender: 'createTime' }
  },
  {
    title: '操作',
    dataIndex: 'action',
    key: 'action',
    align: 'center',
    fixed: 'right',
    scopedSlots: { customRender: 'action' }
  }
]
