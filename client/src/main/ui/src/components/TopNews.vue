<template>
 <img  class="title" src="../../public/title.png" height="187" width="1255"/>

  <div class="top">
<!--      TODO: dynamic transfer of links and content of articles-->
      <img src="/17-g-ry-dla-seniora-jak-si.jpg" height="853" width="1200"/>
      <h1>Tytuł artykułu</h1>
      <p>
        Treść zachęcająca
      </p>
      <a href="#" class="more">Czytaj dalej...</a>
    </div>
</template>

<script>

</script>

<style scoped>
.title {
  margin-top: 2%;
  margin-bottom: -2%;
  min-width: 10%;
  max-width: 300px;
  height: 5%;
  z-index: 1;
}

.top {
  width: 100%;
  float: left;
  background: aliceblue;
  border-radius: 25px;
  font-size: 1vmax;
}

.top img {
  float: left;
  max-width: 58%;
  max-height: 30vmax;
  margin-right: 2%;
  border-bottom-left-radius: 25px;
  border-top-left-radius: 25px;
}

.top h1{
  float: left;
  width: 38%;
  margin-right: 2%;
}
</style>