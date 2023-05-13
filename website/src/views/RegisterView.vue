<script setup>

</script>

<template>
  <body>
  <div class="limiter">
    <img alt="Vue logo" class="logo" src="../assets/globe.png" width="50" height="50"/>
    <div class="wrapper">
      <TheTitle msg="Serwis informacyjny" />
    </div>
    <div class="container-login100">
      <div class="wrap-login100">
<!--        <div class="login100-pic js-tilt" data-tilt>-->
<!--&lt;!&ndash;          <img src="../../public/img-01.png" alt="IMG">&ndash;&gt;-->
<!--        </div>-->

        <form class="login100-form validate-form"  @submit.prevent>
					<span class="login100-form-title">
						Rejestracja
					</span>

          <div class="wrap-input100 validate-input" data-validate = "Valid username is required: ex@abc.xyz">
            <input class="input100" type="text" name="username" placeholder="Login">
            <span class="focus-input100"></span>
            <span class="symbol-input100">
							<i class="fa fa-envelope" aria-hidden="true"></i>
						</span>
          </div>

          <div class="wrap-input100 validate-input" data-validate = "Password is required">
            <input class="input100" type="password" name="pass" placeholder="Hasło">
            <span class="focus-input100"></span>
            <span class="symbol-input100">
							<i class="fa fa-lock" aria-hidden="true"></i>
						</span>
          </div>

          <div class="wrap-input100 validate-input" data-validate = "Name is required">
            <input class="input100" type="text" name="name" placeholder="Imię">
            <span class="focus-input100"></span>
            <span class="symbol-input100">
							<i class="fa fa-lock" aria-hidden="true"></i>
						</span>
          </div>

          <div class="wrap-input100 validate-input" data-validate = "Username is required">
            <input class="input100" type="text" name="surname" placeholder="Nazwisko">
            <span class="focus-input100"></span>
            <span class="symbol-input100">
							<i class="fa fa-lock" aria-hidden="true"></i>
						</span>
          </div>

          <div class="wrap-input100 validate-input" data-validate = "E-mail is required">
            <input class="input100" type="text" name="email" placeholder="E-mail">
            <span class="focus-input100"></span>
            <span class="symbol-input100">
							<i class="fa fa-lock" aria-hidden="true"></i>
						</span>
          </div>

          <div class="container-login100-form-btn" >
            <button class="login100-form-btn" @click="">
              Zarejestruj się
            </button>
          </div>
          <div class="container-login100-form-btn container-login200-form-btn">
            <button class="login100-form-btn login200-form-btn">
              <img src="../../public/google_icon.png" height="48" width="48"/>
              Zarejestruj się przez Google
            </button>
          </div>
          <br>
          <div class="text-center p-t-136">
            <span class="txt1">
              Posiadasz już konto?
						</span>
            <a class="txt2" href="/login">
              Zaloguj się
            </a>
          </div>
          <div class="text-center p-t-136">
            <a class="txt2" href="/">
              Powrót na stronę główną
            </a>
          </div>
        </form>
      </div>
    </div>
  </div>
  </body>
</template>

<style scoped>
@import '../assets/login.css';

.wrap-login100 {
  width: fit-content;
  padding: 100px;
}

@media (max-width: 992px) {
  .wrap-login100 {
    width: fit-content;
    padding: 50px;
  }

  .login100-form {
    width: 100%;
  }

}
</style>