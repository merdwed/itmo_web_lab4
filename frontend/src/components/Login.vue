<template>
  <div class=login>
   <div class="header">
    <center>
    <p>Лабораторная №4</p>
    <h4>Речкалов Михаил Константинович</h4>
    <p><b>группа:</b> P3212 </p>
    <p><b>вариант:</b> 30556</p>
    </center>
    <hr/>
  </div>

  <div class="unprotected" v-if="loginError">
    <h1><b-badge variant="danger">You don't have rights here, mate :D</b-badge></h1>
    <h5>Seams that you don't have access rights... </h5>
  </div>
  <div class="unprotected" v-else>
    <h1><b-badge variant="info">Please login to get access!</b-badge></h1>
    <h5>You're not logged in - so you don't see much here. Try to log in:</h5>

    <form @submit.prevent="callLogin()">
      <input type="text" placeholder="username" v-model="user">
      <input type="password" placeholder="password" v-model="password">
      <b-btn variant="success" type="submit">Login</b-btn>
      <p v-if="error" class="error">Bad login information</p>
    </form>
  </div>
  </div>

</template>

<script>
export default {
  name: 'login',

  data () {
    return {
      loginError: false,
      user: '',
      password: '',
      error: false,
      errors: []
    }
  },
  methods: {
    callLogin() {
      this.errors = [];
      this.$store.dispatch("login", { user: this.user, password: this.password})
        .then(() => {
          this.$router.push('/protected')
        })
        .catch(error => {
          this.loginError = true;
          this.errors.push(error);
          this.error = true
        })
    }
  }
}
</script>
<style scoped>
.header {
    font-family: fantasy; 
    font-size: 24px;
    color: #336; 
}
</style>