<!-- https://vue3-lite-table.vercel.app/usage -->
<!-- TODO:
  1. User has only possibility to change own topics
  2. Change fake data to fetching from database
  3. As admin/redactor possibility to change all topics, approve or reject
-->

<template>

  <div class="properties">
    <div class="input-add">
      <label>Nowy artykuł:</label>
      <!-- <input v-model="newDraftTitle" /> -->
      <router-link :to="{name: 'edit'}"><button>Stwórz</button></router-link>
    </div>
    <div class="input-look">
      <div class="labelCon"><label>Szukaj:</label></div>
      <input type="search" v-model="searchTerm" />
      <button id="searchButton" @click="handleSearch" class="disabled">
        <img :src="SearchImage" alt="Wyszukaj" class="search-icon" />
      </button>
    </div>
  </div>
  <div class="table-context">
    <table-lite
        :is-static-mode="false"
        :columns="table.columns"
        :rows="table.rows"
        :total="table.totalRecordCount"
        :sortable="table.sortable"
        @is-finished="tableLoadingFinish"
        @row-clicked="rowClicked"
        @do-search="doSearch"
    ></table-lite>
  </div>
</template>

<script setup>
import jsCookie from "js-cookie";
import { reactive, ref, computed, watch } from "vue";
import { toast } from "vue-sonner";
import TableLite from 'vue3-table-lite'
import router from "@/router";
import SearchImage from "@/assets/icons8-search.svg";

const data = reactive([]);
const articlesMap = ref(new Map());
const rowCount = ref(0);

var lastSearchTerm = "";
var lastPage = 0;
var lastOrder = "id";
var lastSort = "desc";

var url = '/editorial/draft?';
var size = 10;

const fetchData = async (page = 0, order = "id", sort = "desc", search = "") =>{
  const queryParams = [];
  if (search != ""){
    queryParams.push(`title=${search}`);
  }
  queryParams.push(`sort=${order},${sort}`);
  url = `/editorial/draft?page=${page}&size=${size}&`;
  url += queryParams.join("&");
  try {
    const response = await fetch(url, {
      method: 'GET',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) {
      const text = await response.text();
      console.log(text);
      toast.error("Wystąpił błąd podczas pobierania artykułów")
    }
    else{
      const responseJson = await response.json();
      data.splice(0, data.length);
      for (let i = 0; i < responseJson.length; i++) {
        data.push({
          id: responseJson[i]["id"],
          authorName: responseJson[i]["authorName"],
          title: responseJson[i]["title"],
          dateOfUpdate: responseJson[i]["dateOfUpdate"],
          content: responseJson[i]["content"],
        });
        articlesMap.value.set(data[i]["id"], data[i]);
        // fetchData = data.length;
      }
      rowCount.value = Number(response.headers.get('X-Total-Count'));
    }
  } catch (error) {
    console.log(error);
  }
}

const searchTerm = ref(""); // Search text
fetchData();
// Table config
const table = reactive({
  columns: [
      {
          label: "Tytuł",
          field: "title",
          width: "5%",
          sortable: true,
          display: function (row) {
              return '<span><a href="#" class="topic" topicId="'+row.id+'">'+row.title+'</a></span>'
          },
      },
      {
          label: "Data zaproponowania",
          field: "dateOfUpdate",
          width: "1%",
          sortable: true,
      },
  ],
  rows: computed(() => {
      return data
  }),
  totalRecordCount: computed(() => {
      return rowCount.value;
  }),
  sortable: {
      order: "id",
      sort: "asc",
  }
});

const tableLoadingFinish = () =>{
  table.isLoading = false;
}

const handleSearch = () => {
  lastSearchTerm = searchTerm.value;
  fetchData(lastPage, lastOrder, lastSort, lastSearchTerm);
};

const doSearch = (offset, limit, order, sort) => {
  table.page = offset / limit;
  console.log("doSearch", offset, limit, order, sort);

  lastPage = offset / limit;
  lastOrder = order;
  lastSort = sort;
  lastSearchTerm = searchTerm.value;
  data.splice(0, size);
  size = limit;

  fetchData(offset / limit, order, sort, lastSearchTerm);

};

const rowClicked = (row) => {
  console.log(row);
  let id = Number(row.id);
  sessionStorage.setItem("articleToEdit", JSON.stringify(articlesMap.value.get((id))));
  router.push({name: 'edit', query:{redirected: true, acceptance: "draft"} });
};

watch(searchTerm, (newValue, oldValue) => {
  if (newValue != oldValue && newValue == "") {
    handleSearch();
  }
});

</script>

<style scoped>

@import '../../assets/userLists.css';

::v-deep(.vtl-tbody-tr){
  cursor: pointer;
}

</style>