<script setup>
import TheMainContent from "@/components/TheMainContent.vue";
import TheNavBar from "@/components/TheNavBar.vue";
import TheTitle from "@/components/TheTitle.vue";
import TheRightSection from "@/components/TheRightSection.vue";
import Footer from "@/components/Footer.vue";
import Editor from "../components/Editor.vue";
</script>

<template>
<!--MARK: 1. HEADER-->
  <header>
    <img alt="Vue logo" class="logo" src="../assets/globe.png" width="50" height="50" />

    <div class="wrapper">
      <TheTitle msg="Serwis informacyjny" />
    </div>

  </header>

<!--MARK: 2. NAVIGATION BAR-->
  <nav>
    <TheNavBar />
  </nav>

  <div>
    <!--MARK: 3. MAIN CONTENT-->
    <main>
      <Editor />
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
}

header .wrapper {
  display: flex;
  place-items: flex-start;
  flex-wrap: wrap;
}

footer {
  width: 100%;
  position: absolute;
  height: 100px;
  bottom: 0;

}

</style>
