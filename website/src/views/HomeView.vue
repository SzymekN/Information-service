<script setup>
import TheNavBar from "@/components/TheNavBar.vue";
import TheTitle from "@/components/TheTitle.vue";
import TheRightSection from "@/components/TheRightSection.vue";
import Footer from "@/components/Footer.vue";


</script>

<template>
<!--MARK: 1. HEADER-->
  <header>
    <img alt="Vue logo" class="logo" src="../assets/globe.png" width="50" height="50" />

    <div class="wrapper">
      <TheTitle msg="Serwis informacyjny" />
    </div>

    <div class="buttons">
      <button><a href="#">Zaloguj/Zarejestruj</a></button>
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
    <section>
      <TheRightSection />
    </section>
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

main {
  width: 80%;
  margin-top: 7.5rem;
  margin-left: 1%;
  justify-content: left;
  min-height: 100%;
  padding: 1% 1% 0 1%;
  position: relative;
  float: left;
  margin-bottom: 4rem;
}

section {
  width: 17%;
  background-color: rgba(240, 248, 255, 0.2);
  top: 0;
  margin-top: 7.5rem;
  margin-right: 1%;
  justify-content: center;
  min-height: 100%;
  position: relative;
  padding: 0 1% 0 1%;
  float: left;
  font-size: 1vmax;
}

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
