<script setup>
import TheNavBar from "@/components/main-layout/TheNavBar.vue";
import TheTitle from "@/components/main-layout/TheTitle.vue";
import Footer from "@/components/main-layout/Footer.vue";
import LoginButton from "@/components/buttons/LoginButton.vue";


</script>
<template>
<!--MARK: 1. HEADER-->
  <Toaster  richColors position="top-center" closeButton />
  <header>
  <router-link to="/">
    <img alt="Vue logo" class="logo" src="../assets/globe.png" width="50" height="50" />
  </router-link>
    <div class="wrapper">
      <TheTitle msg="Serwis informacyjny" />
    </div>

    <div class="buttons">
      <LoginButton></LoginButton>
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
  /*background: linear-gradient(#ffffff, aliceblue);*/
  display: flex;
  place-items: center;
  width: 100%;
  position: fixed;
  top: 0;
  height: 5rem;
  opacity: 98%;
  z-index: 1;
  background: white;
}

.logo {
  display: block;
  margin: 0.2rem 1.2rem 0.2rem;
}

nav {
  background: linear-gradient(#ffffff, aliceblue);
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
  color: white;

}


a {
  color: #666666;
  display: block;
  padding: 0.3rem;
}

a:hover{
  color: #fff;
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
