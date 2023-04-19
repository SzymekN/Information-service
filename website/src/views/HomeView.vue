<script setup>
import TheNavBar from "@/components/main-layout/TheNavBar.vue";
import TheTitle from "@/components/main-layout/TheTitle.vue";
import Footer from "@/components/main-layout/Footer.vue";
import list from "../data/login.json"
import jsCookie from "js-cookie";

  const logout = () => {
    //   fetch('/logout', {
    //   method: 'GET',
    //   credentials: 'same-origin',
    // })
    // .then(response => {
    //   // Handle the response from the server
    //   if (response.ok) {
    //     // Logout successful, do something
    //     console.log('Logged out successfully');
    //   } else {
    //     // Logout failed, do something
    //     console.error('Logout failed');
    //   }
    // })
    // .catch(error => {
    //   // Handle errors that may occur during the request
    //   console.error('Logout failed', error);
    // });
    jsCookie.remove('role');
    console.log(jsCookie.get('role'));
    window.location.href = '/home';

  }

</script>

<template>
<!--MARK: 1. HEADER-->
  <header>
    <img alt="Vue logo" class="logo" src="../assets/globe.png" width="50" height="50" />

    <div class="wrapper">
      <TheTitle msg="Serwis informacyjny" />
    </div>

    <div class="buttons" v-for="item in list">
      <button><router-link :to="item.link" v-if="!jsCookie.get('role')"><a>{{item.text}}</a></router-link></button>
      <button><router-link :to="'#'" v-if="jsCookie.get('role')" @click="logout()"><a>{{"Wyloguj"}}</a></router-link></button>
    </div>
  </header>

<!--MARK: 2. NAVIGATION BAR-->
  <nav>
    <TheNavBar />
  </nav>

  <div>
    <!--MARK: 3. MAIN CONTENT-->
    <main>
      <RouterView/>
    </main>

    <!--MARK: 4. DYNAMIC SECTION-->
    <!-- <section>
      <TheRightSection />
    </section> -->
  </div>


<!--  MARK: 5. FOOTER-->
  <footer>
    <Footer/>
  </footer>

</template>

<style scoped>
header {
  line-height: 1.5;
  margin: 0;
  background: linear-gradient(#ffffff, aliceblue);
  display: flex;
  place-items: center;
  width: 100%;
  position: fixed;
  top: 0;
  height: 5rem;
  opacity: 98%;
  z-index: 1;
}

.logo {
  display: block;
  margin: 0.2rem 1.2rem 0.2rem;
}

nav {
  background: linear-gradient(aliceblue, #ffffff);
  line-height: 1.5;
  display: flex;
  place-items: center;
  width: 100%;
  position: fixed;
  top: 0;
  margin-top: 5rem;
  opacity: 98%;
  z-index: 1;
}

/* main {
  width: 80%;
  margin-top: 7.5rem;
} */

header .wrapper {
  display: flex;
  place-items: flex-start;
  flex-wrap: wrap;
  flex-direction: column;
  flex: 2;
}

footer {
  width: 100%;
  position: relative;
  clear: both;
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

@media (max-width: 640px){
  section{
    display: none;
  }
  main {
    width: 100%;
    margin: 1rem 0;
  }
}
</style>
