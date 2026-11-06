// 获取当前日期（XXXX-XX-XX）
export function getCurDate() {
    let now = new Date();
    let year = now.getFullYear();
    let month = now.getMonth() + 1;
    let day = now.getDate();
    month = month < 10 ? "0" + month : month;
    day = day < 10 ? "0" + day : day;
    return year + "-" + month + "-" + day;
}

// 获取当前时间（XX:XX:XX）
export function getCurTime() {
    let now = new Date();
    let hours = now.getHours();
    let minutes = now.getMinutes();
    let seconds = now.getSeconds();
    hours = hours < 10 ? "0" + hours : hours;
    minutes = minutes < 10 ? "0" + minutes : minutes;
    seconds = seconds < 10 ? "0" + seconds : seconds;
    return hours + ":" + minutes + ":" + seconds;
}

// 向 sessionStorage 中存储一个 JSON 对象
export function setSessionStorage(keyStr, value) {
    sessionStorage.setItem(keyStr, JSON.stringify(value));
}

// 从 sessionStorage 中获取一个 JSON 对象（取不到时返回 null）
export function getSessionStorage(keyStr) {
    var str = sessionStorage.getItem(keyStr);
    if (str == '' || str == null || str == 'null' || str == undefined) {
        return null;
    } else {
        return JSON.parse(str);
    }
}

// 从 sessionStorage 中移除一个 JSON 对象
export function removeSessionStorage(keyStr) {
    sessionStorage.removeItem(keyStr);
}

// 向 localStorage 中存储一个 JSON 对象
export function setLocalStorage(keyStr, value) {
    localStorage.setItem(keyStr, JSON.stringify(value));
}

// 从 localStorage 中获取一个 JSON 对象（取不到时返回 null）
export function getLocalStorage(keyStr) {
    var str = localStorage.getItem(keyStr);
    if (str == '' || str == null || str == 'null' || str == undefined) {
        return null;
    } else {
        return JSON.parse(str);
    }
}

// 从 localStorage 中移除一个 JSON 对象
export function removeLocalStorage(keyStr) {
    localStorage.removeItem(keyStr);
}