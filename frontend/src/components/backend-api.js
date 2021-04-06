import axios from 'axios'

const AXIOS = axios.create({
  baseURL: `/api`,
  timeout: 100000
});


export default {
    hello() {
        return AXIOS.get(`/hello`);
    },
    getUser(userId) {
        return AXIOS.get(`/user/` + userId);
    },
    createUser(user, password) {
        return AXIOS.post(`/user`, {username:user,password:password});
    },
    auth(user, password){
        return AXIOS.get(`/auth?username=${user}`,{
            auth: {
                username: user,
                password: password
            }});
    },
    getTable(user, password,userId) {
        return AXIOS.get(`/secured/`+userId,{
            auth: {
                username: user,
                password: password
            }});
    },
    putTable(user,password,userId,coordX,coordY){
       

        return AXIOS.post(`/secured/`+userId,{
                coordX:coordX,
                coordY:coordY,
                userId:userId
            },{
            auth: {
                username: user,
                password: password
            }
            }
            );
    }
}


