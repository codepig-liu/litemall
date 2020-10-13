import request from '@/utils/request'

export function listPartner(query) {
  return request({
    url: '/partner/list',
    method: 'get',
    params: query
  })
}

export function createPartner(data) {
  return request({
    url: '/partner/create',
    method: 'post',
    data
  })
}

export function readPartner(data) {
  return request({
    url: '/partner/read',
    method: 'get',
    data
  })
}

export function updatePartner(data) {
  return request({
    url: '/partner/update',
    method: 'post',
    data
  })
}

export function deletePartner(data) {
  return request({
    url: '/partner/delete',
    method: 'post',
    data
  })
}
