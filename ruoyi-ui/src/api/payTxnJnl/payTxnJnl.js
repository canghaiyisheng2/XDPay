import request from '@/utils/request'

// 查询支付流水列表
export function listPayTxnJnl(query) {
  return request({
    url: '/payTxnJnl/payTxnJnl/list',
    method: 'get',
    params: query
  })
}

// 查询支付流水详细
export function getPayTxnJnl(id) {
  return request({
    url: '/payTxnJnl/payTxnJnl/' + id,
    method: 'get'
  })
}

// 新增支付流水
export function addPayTxnJnl(data) {
  return request({
    url: '/payTxnJnl/payTxnJnl',
    method: 'post',
    data: data
  })
}

// 修改支付流水
export function updatePayTxnJnl(data) {
  return request({
    url: '/payTxnJnl/payTxnJnl',
    method: 'put',
    data: data
  })
}

// 删除支付流水
export function delPayTxnJnl(id) {
  return request({
    url: '/payTxnJnl/payTxnJnl/' + id,
    method: 'delete'
  })
}
