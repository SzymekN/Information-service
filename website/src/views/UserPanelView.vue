<script setup>
import TheTitle from "@/components/main-layout/TheTitle.vue";
import UserPanelNav from "@/components/user-layout/UserPanelNav.vue";
import jsCookie from "js-cookie";
import { Toaster } from "vue-sonner";

const hasRole = () => {
  if (jsCookie.get("ROLE"))
    return  (atob(jsCookie.get("ROLE")));
  else return false;
};
</script>

<template>
  <Toaster richColors/>
  <!--MARK: 1. HEADER-->
  <header>
    <router-link to="/">
      <img alt="Vue logo" class="logo" src="../assets/globe.png" width="50" height="50" />
    </router-link>

    <div class="wrapper">
      <TheTitle msg="Serwis informacyjny" />
    </div>

    <h1>WITAJ, {{hasRole()}}</h1>

    <div class="buttons">
      <button><a href="#">Wyloguj się</a></button>
    </div>
  </header>

  <!--MARK: 2. NAVIGATION BAR-->
  <nav>
    <UserPanelNav/>
  </nav>

  <div>
    <!--MARK: 3. MAIN CONTENT-->
    <main>
      <RouterView/>
    </main>
  </div>

</template>

<style scoped>
header {
  line-height: 1.5;
  margin: 0;
  background-color: white;
  display: flex;
  place-items: center;
}

.logo {
  display: block;
  margin: 0.2rem 1.2rem 0.2rem;
}

nav {
  width: 20%;
  background: linear-gradient(rgba(200, 216, 241), #c8d8f1,#c8d8f1, white);
  top: 0;
  justify-content: center;
  min-height: 150vh;
  position: relative;
  padding: 1%;
  float: left;
}

main {
  width: 80%;
  justify-content: left;
  margin: 0;
  min-height: 150vh;
  padding: 1% 1% 0 1%;
  position: relative;
  float: right;
  background: linear-gradient(rgba(240, 248, 255, 0.7), rgba(240, 248, 255, 0.7),rgba(240, 248, 255, 0.7), white);
}

header .wrapper {
  display: flex;
  place-items: flex-start;
  flex-wrap: wrap;
}

h1 {
  font-weight: 200;
  font-size: 25px;
  flex: 2;
  text-align: center;
}

@media (max-width: 1180px){
  h1 {
    width: 45%;
  }
}


@media (max-width: 700px){
  nav {
    width: 100%;
    min-height: fit-content;
    background: linear-gradient(#c8d8f1, #c8d8f1);
  }

  main {
    width: 100%;
    background: linear-gradient(rgba(240, 248, 255, 0.7), rgba(240, 248, 255, 0.7));
  }

  header .wrapper {
    display: none;
  }

}

.buttons {
  margin: 0.5rem 2rem;
  align-content: center;
}

.buttons button {
  background-color: rgba(240, 248, 255);
  border: none;
  font-size: 0.9rem;
  width: 100%;
}

.buttons button:hover {
  background-color: #c8d8f1;
}

a {
  display: block;
  padding: 0.3rem;
}
</style>
