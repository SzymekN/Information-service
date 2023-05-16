<script setup>
import TheTitle from "@/components/main-layout/TheTitle.vue";
import {Toaster, toast } from 'vue-sonner'
import {useRouter} from 'vue-router'

const router = useRouter()

const register = async () =>{

  const username = document.querySelector('input[name="username"]').value;
  const password = document.querySelector('input[name="password"]').value;
  const name = document.querySelector('input[name="name"]').value;
  const surname = document.querySelector('input[name="surname"]').value;
  const email = document.querySelector('input[name="email"]').value;

  let requestBody = JSON.stringify({
          username: username,
          password: password,
          name: name,
          surname: surname,
          email: email
        })

  try{
    const url = '/client/registration';
    const response = await fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: requestBody,
    });

    if (!response.ok) {
      const text = await response.json();
      for (const [key, value] of Object.entries(text)) {
        setTimeout(() => toast.error(`${key}: ${value}`), 10)
      }
      setTimeout(() => toast.error("Wystąpił błąd podczas rejestracji"), 100)
    }
    else
      router.push('/')

  } catch (error) {
    toast.error("Wystąpił błąd podczas rejestracji")
  }

}

</script>

<template>
    <Toaster  richColors position="top-center" closeButton :visibleToasts="9"/>

  <body>
  <div class="limiter">
    <img alt="Vue logo" class="logo" src="../assets/globe.png" width="50" height="50"/>
    <div class="wrapper">
      <TheTitle msg="Serwis informacyjny" />
    </div>
    <div class="container-login100">
      <div class="wrap-login100">

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
            <input class="input100" type="password" name="password" placeholder="Hasło">
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
            <button class="login100-form-btn" @click="register()">
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
            <router-link :to="`/login`">
              <a class="txt2">
                Zaloguj się
              </a>
            </router-link>
          </div>
          <div class="text-center p-t-136">
            <router-link :to="`/`">
              <a class="txt2" href="/">
                Powrót na stronę główną
              </a>
            </router-link>
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