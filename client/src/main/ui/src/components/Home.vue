<script setup>
import TheMainContent from "@/components/TheMainContent.vue";
import TheNavBar from "@/components/TheNavBar.vue";
import TheTitle from "@/components/TheTitle.vue";
import LoginButton from "@/components/LoginButton.vue";
import TheRightSection from "@/components/TheRightSection.vue";
</script>

<template>
<!--MARK: 1. HEADER-->
  <header>
    <img alt="Vue logo" class="logo" src="../assets/globe.png" width="50" height="50" />

    <div class="wrapper">
      <TheTitle msg="Serwis informacyjny" />
    </div>

    <div class="login">
      <LoginButton msg="Zaloguj się" />
      <LoginButton msg="Zarejestruj się" />
    </div>

  </header>

<!--MARK: 2. NAVIGATION BAR-->
  <nav>
    <div class="navBar">
      <TheNavBar msg="Strona główna"/>
      <TheNavBar msg="Sport"/>
      <TheNavBar msg="Edukacja"/>
      <TheNavBar msg="Technologia"/>
      <TheNavBar msg="Pogoda"/>
      <TheNavBar msg="Biznes"/>
      <TheNavBar msg="Etc"/>
    </div>
  </nav>

<!--MARK: 3. MAIN CONTENT-->
  <main>
    <TheMainContent />
  </main>

<!--MARK: 4. DYNAMIC SECTION-->
  <section>
    <TheRightSection />
  </section>


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
}

main {
  width: 80%;
  top: 0;
  margin-top: 7.5rem;
  margin-left: 1%;
  justify-content: left;
  min-height: 100%;
  position: fixed;
  padding: 1% 1% 0 1%;
  float: left;
}

section {
  width: 17%;
  background-color: aliceblue;
  top: 0;
  margin-top: 7.5rem;
  margin-left: 82%;
  margin-right: 1%;
  justify-content: center;
  min-height: 100%;
  position: fixed;
  padding: 0 1% 0 1%;
}

header .wrapper {
  display: flex;
  place-items: flex-start;
  flex-wrap: wrap;
}

.login {
  float: right;
  margin-left: auto;
  margin-right: 0.5rem;
}

</style>
