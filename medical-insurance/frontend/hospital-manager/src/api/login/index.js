import request from '../util/request';

/**
 * 登录
 * @param data
 */
export function login(data){
  return request({
    url: '/login',
    method: 'post',
    params: data,
    headers: {
      Authorization: 'Basic bWFsbC1hZG1pbi13ZWI6MTIzNDU2' 
    }
  });
}

/**
 * 注销
 */
export function logout() {
  return request({
    url: '/logout',
    method: 'delete'
  });
}
 