<!-- https://vue3-lite-table.vercel.app/usage -->
<template>
  <div class="properties">
    <form @submit.prevent>
      <div class="input-add">
        <div class="labelCon"><label>Nowy temat:</label></div><input v-model="newTopicProposal" type="search"/>
        <button @click="addTopic">Dodaj</button>
      </div>
    </form>
    <div class="input-look">
      <form @submit.prevent>
        <div class="labelCon"><label>Szukaj:</label></div><input v-model="searchTerm" />
        <button id="searchButton" @click="handleSearch" class="disabled">
          <img :src="SearchImage" alt="Wyszukaj" class="search-icon" />
        </button>
      </form>
    </div>
    <label for="state_input">Stan:</label>
    <select v-model="state" class="state_input">
      <option value="All" selected>All</option>
      <option value="PENDING">PENDING</option>
      <option value="ACCEPTED">ACCEPTED</option>
      <option value="DECLINED">DECLINED</option>
    </select>
  </div>
  <div class="table-context">
    <table-lite
        :is-static-mode="false"
        :columns="table.columns"
        :rows="table.rows"
        :total="table.totalRecordCount"
        :sortable="table.sortable"
        @is-finished="tableLoadingFinish"
        @row-clicked="tableLoadingFinish"
        @do-search="doSearch"
    ></table-lite>
  </div>
</template>

<script setup>
import jsCookie from "js-cookie";
import { reactive, ref, computed, watch } from "vue";
import { toast } from "vue-sonner";
import SearchImage from "@/assets/icons8-search.svg";
import TableLite from 'vue3-table-lite'

const data = reactive([]);
var url = '/editorial/proposal?';
var size = 10;

const searchTerm = ref(""); // Search text
const newTopicProposal = ref(""); // user input with proposition
const acceptance = ref(""); 
const rowCount = ref(0);
const state = ref("All"); 

var lastSearchTerm = "";
var lastPage = 0;
var lastOrder = "id";
var lastSort = "desc";


const fetchData = async (page = 0, order = "id", sort = "desc", search = "") =>{
  const queryParams = [];
  if (search != ""){
    queryParams.push(`title=${search}`);
  }
  if (state.value != "All"){
    queryParams.push(`acceptance=${state.value}`);
  }
  queryParams.push(`sort=${order},${sort}`);
  url = `/editorial/proposal?page=${page}&size=${size}&`;
  url += queryParams.join("&");
  console.log(url)
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
      console.log(text)
      toast.error("Nie udało się pobrać danych")
    }
    else{
      const responseJson = await response.json();
      data.splice(0, size);
      for (let i = 0; i < responseJson.length; i++) {
        data.push({
          id: responseJson[i]["id"],
          authorName: responseJson[i]["authorName"],
          title: responseJson[i]["title"],
          dateOfUpdate: responseJson[i]["dateOfUpdate"],
          acceptance: responseJson[i]["acceptance"],
        });
        // fetchData = data.length;
      }
      rowCount.value = Number(response.headers.get('X-Total-Count'));
    }
  } catch (error) {
    console.log(error);
  }
}


fetchData();
// Table config
const table = reactive({
  columns: [
      {
          label: "Użytkownik",
          field: "authorName",
          width: "1%",
      },
      {
          label: "Temat",
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
      {
          label: "Stan",
          field: "acceptance",
          width: "1%",
          sortable: true,
          display: function (row) {
            let color = "#e8b53f";
            let colorBg = "#fff"
            if (row.acceptance == "PENDING"){
              color = "#e8b53f";
              colorBg = "rgba(245,245,220,0.5)"
            }
            if (row.acceptance == "ACCEPTED"){
              color = "#05a32f";
              colorBg = "rgba(217,246,198,0.5)"
            }
            if (row.acceptance == "DECLINED"){
              color = "#a31505";
              colorBg = "rgba(252,207,211,0.5)"
            }

            if (atob(jsCookie.get('ROLE')) == 'ROLE_ADMIN' || atob(jsCookie.get('ROLE')) == 'ROLE_REDACTOR'){
              var acceptanceSelect = document.createElement("select");
              acceptanceSelect.setAttribute("name", "acceptance");
              acceptanceSelect.setAttribute("id", "acceptance");
              acceptanceSelect.setAttribute("class", "acceptance");
              acceptanceSelect.setAttribute("topicTitle", row.title);
              acceptanceSelect.setAttribute("topicId", row.id);
              acceptanceSelect.setAttribute("style", "width: 100%;\n" +
                  "    border-radius: 5px;\n" +
                  "    padding: 5%;\n" +
                  "    background: " +colorBg + "; color:"+color);

              let pendingOption = document.createElement("option");
              pendingOption.setAttribute("value", "PENDING");
              pendingOption.innerHTML = "PENDING";
              pendingOption.setAttribute("style", "color:#e8b53f");
              if (row.acceptance === "PENDING"){
                pendingOption.selected = true;
                pendingOption.setAttribute("selected", "selected");
              }

              let acceptedOption = document.createElement("option");
              acceptedOption.setAttribute("value", "ACCEPTED");
              acceptedOption.innerHTML = "ACCEPTED";
              acceptedOption.setAttribute("style", "color:#05a32f");
              if (row.acceptance === "ACCEPTED"){
                acceptedOption.selected = true;
                acceptedOption.setAttribute("selected", "selected");
              }

              let declinedOption = document.createElement("option");
              declinedOption.setAttribute("value", "DECLINED");
              declinedOption.innerHTML = "DECLINED";
              declinedOption.setAttribute("style", "color:#a31505");
              if (row.acceptance === "DECLINED"){
                declinedOption.selected = true;
                declinedOption.setAttribute("selected", "selected");
              }

              acceptanceSelect.appendChild(pendingOption);
              acceptanceSelect.appendChild(acceptedOption);
              acceptanceSelect.appendChild(declinedOption);

              return acceptanceSelect.outerHTML;

            }

            return '<span style=color:'+color+'>'+row.acceptance+'</span>'
          },
      },
  ],
  rows: computed(() => {
      return data
  }),
  totalRecordCount: computed(() => {
      return rowCount.value
  }),
  sortable: {
      order: "id",
      sort: "desc",
  }
});


// Change topic
function changeTopicListener(){
  let newTopic = prompt("Podaj nowy temat")
  if (newTopic == null || newTopic == "")
    return;
  let id = this.getAttribute('topicId');
  let title = this.getAttribute('topicTitle');
  let acceptance = "PENDING"
  if (atob(jsCookie.get('ROLE')) == 'ROLE_ADMIN' || atob(jsCookie.get('ROLE')) == 'ROLE_REDACTOR')
    acceptance = "ACCEPTED"

  const request = {
      id: id,
      title: newTopic,
      acceptance: acceptance,
  }
  changeProposalFetch(request);
  tableLoadingFinish()
}

// Make request
const changeProposalFetch = async(bodyStruct) =>{

  console.log(bodyStruct)
  table.isLoading = true;
  try {
  const response = await fetch(url, {
    method: 'PUT',
    credentials: 'include',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(bodyStruct),
  });

  if (!response.ok) {
    const text = await response.text();
    console.log(text)
    toast.error("Nie udało się zmienić propozycji")
  }
  else{
    toast.success("Zmieniono propozycję")
    for (let i = 0; i < data.length; i++) {
      if (data[i].id == bodyStruct.id){
        console.log(data[i])
        data[i].title = bodyStruct.title;
        data[i].acceptance = bodyStruct.acceptance;
        console.log(data[i])
        break;
      }
    }
  }
  } catch (error) {
    toast.error("Nie udało się zmienić propozycji")
    console.log(error);
  }

  tableLoadingFinish()

}

// Change state
function changeStateListener(){

  console.log(this.value)
  let id = this.getAttribute('topicId');
  let title = this.getAttribute('topicTitle');
  const request = {
      id: id,
      title: title,
      acceptance: this.value,
  }

  changeProposalFetch(request)
  tableLoadingFinish()
}

function addListeners(className, listenerFunction, listenerType){
  let elements = document.getElementsByClassName(className)

  Array.prototype.forEach.call(elements, function (element) {
  //checking if cell already has onclick assigned
  if(element.getAttribute('listener') !== 'true'){
      element.setAttribute('listener', 'true');
      element.addEventListener(listenerType, listenerFunction);
  }
  });

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

watch(searchTerm, (newValue, oldValue) => {
  if (newValue != oldValue && newValue == "") {
    handleSearch();
  }
});

watch(state, (newValue, oldValue) => {
  if (newValue != oldValue) {
    handleSearch();
  }
});

const tableLoadingFinish = (page = 1) => {
  
  // table.page = page;
  table.isLoading = false;
  addListeners("topic", changeTopicListener, "click");
  if (atob(jsCookie.get('ROLE')) == 'ROLE_ADMIN' || atob(jsCookie.get('ROLE')) == 'ROLE_REDACTOR')
      addListeners("acceptance", changeStateListener, "change");
};

const addTopic = async () =>{

  const newTopic = newTopicProposal.value;
  const request = {
      title: newTopic,
  }

  console.log(document.cookie)

  try {
      const url = '/editorial/proposal';

      const response = await fetch(url, {
      method: 'POST',
      credentials: 'include',
      headers: {
          'Content-Type': 'application/json',
      },
      body: JSON.stringify(request),
      });

      if (!response.ok) {
        const text = await response.text();
        toast.error("Nie udało się dodać tematu")
        console.log(text)
      }

      else{
        toast.success("Dodano temat")
        fetchData();
        newTopicProposal.value = "";
      }
  } catch (error) {
      console.log(error);
  }


}


</script>

<style scoped>
@import '../../assets/userLists.css';
.vtl-paging-info.col-sm-12.col-md-4,
.vtl-paging-change-div.col-sm-12.col-md-4,
.vtl-paging-pagination-ul.vtl-pagination li:first-child,
.vtl-paging-pagination-ul.vtl-pagination li:last-child
{
    display:none;
}
</style>