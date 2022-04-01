import request from '@/utils/request'

// 查询订单支付方式信息列表
export function listPayMethod(query) {
  return request({
    url: '/payMethod/payMethod/list',
    method: 'get',
    params: query
  })
}

// 查询订单支付方式信息详细
export function getPayMethod(id) {
  return request({
    url: '/payMethod/payMethod/' + id,
    method: 'get'
  })
}

// 新增订单支付方式信息
export function addPayMethod(data) {
  return request({
    url: '/payMethod/payMethod',
    method: 'post',
    data: data
  })
}

// 修改订单支付方式信息
export function updatePayMethod(data) {
  return request({
    url: '/payMethod/payMethod',
    method: 'put',
    data: data
  })
}

// 删除订单支付方式信息
export function delPayMethod(id) {
  return request({
    url: '/payMethod/payMethod/' + id,
    method: 'delete'
  })
}
