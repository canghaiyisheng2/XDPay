import request from '@/utils/request'

// 查询订单请求数记录列表
export function listPayOrderCount(query) {
  return request({
    url: '/payOrderCount/payOrderCount/list',
    method: 'get',
    params: query
  })
}

// 查询订单请求数记录详细
export function getPayOrderCount(id) {
  return request({
    url: '/payOrderCount/payOrderCount/' + id,
    method: 'get'
  })
}

// 新增订单请求数记录
export function addPayOrderCount(data) {
  return request({
    url: '/payOrderCount/payOrderCount',
    method: 'post',
    data: data
  })
}

// 修改订单请求数记录
export function updatePayOrderCount(data) {
  return request({
    url: '/payOrderCount/payOrderCount',
    method: 'put',
    data: data
  })
}

// 删除订单请求数记录
export function delPayOrderCount(id) {
  return request({
    url: '/payOrderCount/payOrderCount/' + id,
    method: 'delete'
  })
}
