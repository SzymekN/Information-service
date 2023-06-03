<script setup>
import TheTitle from "@/components/main-layout/TheTitle.vue";
import {Toaster, toast } from 'vue-sonner'
import Cookie from "js-cookie";
import {useRouter} from 'vue-router'

  const router = useRouter()

  const getUser= async ()=>{
        try{
          const response = await fetch('/editorial/actions/user/info', {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json',
            },
          }).catch(error=>{if(error.reponse.status===401){
            toast.error("Nie potwierdzono linka w mailu.")
          }
        });
          if (!response.ok) {
            const text = await response.text();
            console.log(text)
          }
          else {
            const responseJson = await response.json();
            return responseJson;
          }
        }
        
        catch(error){
          console.log(error);
        }
    };
  //TODO: change it to request to backend instead of fake
  const login = async () =>{
    
    const username = document.querySelector('input[name="username"]').value;
    const password = document.querySelector('input[name="password"]').value;

    let requestBody = JSON.stringify({
            username: username,
            password: password,
          })
  
    console.log(requestBody);
    // make request to backend
    try {
      const url = '/client/login/v2';

      const response = await fetch(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: requestBody,
    });

    if (!response.ok) {
      const text = await response.text();
      toast.error(text)
    }
    else{
      //  const responseJson = await response.json();
        var user=await getUser();
      sessionStorage.setItem("user", JSON.stringify(user));
      sessionStorage.setItem("loginMonitShown", "false");
      router.push('/')
    }

    } catch (error) {
      console.log(error);
    }

    // Do something with the username and password values
    if (username == "admin"){
      Cookie.set('role', 'admin')
      router.push('/')
    }

    if (username == "journalist"){
      Cookie.set('role', 'journalist')
      router.push('/')
    }

    if (username == "redactor"){
      Cookie.set('role', 'redactor')
      router.push('/')
    }

    if (username == "corrector"){
      Cookie.set('role', 'corrector')
      router.push('/')
    }

    console.log(username, password);
  }

  const loginGoogle = async () =>{
    try{
    const url = '/client/login/google';
    const response = await fetch(url, {
      method: 'GET',
      headers: {
          'Content-Type': 'application/json',
      },
    });
    const res = await response.json();
    console.log(res);
    if (!response.ok) {
      toast.error(text)
    }
    else
      window.location.href = res.url;
    } catch (error) {
      console.log(error);
    }
  }
  
</script>

<template>
  <Toaster  richColors position="top-center" closeButton />
  <body>
  <div class="limiter">
    <router-link to="/">
      <img alt="Vue logo" class="logo" src="../assets/globe.png" width="50" height="50" />
    </router-link>
    <div class="wrapper">
      <TheTitle msg="Serwis informacyjny" />
    </div>
    <div class="container-login100">
      <div class="wrap-login100">
        <div class="login100-pic js-tilt" data-tilt>
          <img src="../../public/img-01.png" alt="IMG">
        </div>

        <form class="login100-form validate-form"  @submit.prevent>
					<span class="login100-form-title">
						Logowanie
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

          <div class="container-login100-form-btn" >
            <button class="login100-form-btn" @click="login()">
              Zaloguj się
            </button>
          </div>
          <div class="container-login100-form-btn container-login200-form-btn">
            <button class="login100-form-btn login200-form-btn" @click="loginGoogle()">
              <img src="../../public/google_icon.png" height="48" width="48"/>
              Zaloguj się przez Google
            </button>
          </div>
          <br>
          <div class="text-center p-t-136">
            <span class="txt1">
              Nie masz jeszcze konta?
						</span>
            <router-link :to="`/register`">
              <a class="txt2">
                Zarejestruj się
              </a>
            </router-link>
          </div>
          <div class="text-center p-t-136">
            <router-link :to="`/`">
              <a class="txt2">
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
</style>