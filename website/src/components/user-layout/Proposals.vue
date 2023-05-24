<!-- https://vue3-lite-table.vercel.app/usage -->
<!-- TODO:
    1. User has only possibility to change own topics
    2. Change fake data to fetching from database
    3. As admin/redactor possibility to change all topics, approve or reject
-->

<template>
  <!-- <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous"> -->

  <div class="properties">
    <div class="input-add">
      <label>Nowy temat:</label><input v-model="newTopicProposal" />
      <button @click="addTopic">Dodaj</button>
    </div>
    <div class="input-look">
      <label>Szukaj:</label><input v-model="searchTerm" />
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
        @row-clicked="tableLoadingFinish"
        @do-search="doSearch"
    ></table-lite>
  </div>
</template>

<script setup>
import jsCookie from "js-cookie";
import { reactive, ref, computed } from "vue";
import { toast } from "vue-sonner";
import TableLite from 'vue3-table-lite'

// TODO: replace with fetched data
// Fake Data for 'asc' sortable
const data = reactive([]);
var url = '/editorial/proposal?';
// var page = 0;
var size = 10;

const fetchProposals = async (page = 1) =>{
  try {
    const response = await fetch(url+`page=${page}&size=${size}`, {
      method: 'GET',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) {
      const text = await response.text();
      toast.error(text)
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
          acceptance: responseJson[i]["acceptance"],
        });
        // fetchProposals = data.length;
      }
      table.page = page;
      console.log(table.page)
    }
  } catch (error) {
    console.log(error);
  }
}

const searchTerm = ref(""); // Search text
const newTopicProposal = ref(""); // user input with proposition
fetchProposals();
// Table config
const table = reactive({
  columns: [
      {
          label: "Użytkownik",
          field: "authorName",
          width: "1%",
          sortable: true,
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
            if (row.acceptance == "PENDING")
              color = "#e8b53f";
            if (row.acceptance == "ACCEPTED")
              color = "#05a32f";
            if (row.acceptance == "DECLINED")
              color = "#a31505";
            
            if (atob(jsCookie.get('ROLE')) == 'ROLE_ADMIN' || atob(jsCookie.get('ROLE')) == 'ROLE_REDACTOR'){
              var acceptanceSelect = document.createElement("select");
              acceptanceSelect.setAttribute("name", "acceptance");
              acceptanceSelect.setAttribute("id", "acceptance");
              acceptanceSelect.setAttribute("class", "acceptance");
              acceptanceSelect.setAttribute("topicTitle", row.title);
              acceptanceSelect.setAttribute("topicId", row.id);
              acceptanceSelect.setAttribute("style", "width:100%;color:"+color);

              let pendingOption = document.createElement("option");
              pendingOption.setAttribute("value", "PENDING");
              pendingOption.innerHTML = "PENDING";
              pendingOption.setAttribute("style", "color:orange");
              if (row.acceptance === "PENDING"){
                pendingOption.selected = true;
                pendingOption.setAttribute("selected", "selected");
              }

              let acceptedOption = document.createElement("option");
              acceptedOption.setAttribute("value", "ACCEPTED");
              acceptedOption.innerHTML = "ACCEPTED";
              acceptedOption.setAttribute("style", "color:green");
              if (row.acceptance === "ACCEPTED"){
                acceptedOption.selected = true;
                acceptedOption.setAttribute("selected", "selected");
              }

              let declinedOption = document.createElement("option");
              declinedOption.setAttribute("value", "DECLINED");
              declinedOption.innerHTML = "DECLINED";
              declinedOption.setAttribute("style", "color:red");
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
      return 80
  }),
  sortable: {
      order: "id",
      sort: "asc",
  },
  page: 1,
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
    toast.error(text)
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
    table.page=1;
  }
  } catch (error) {
    toast.error(error)
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

const doSearch = (offset, limit, order, sort) => {
  console.log(offset, limit, order, sort);
  table.page = offset / 10 + 1;
  console.log(offset / 10 + 1);
  console.log(table.page);
  
  if(offset + limit > data.length / 2)
    fetchProposals(offset / 10 + 1);
  table.page = offset / 10 + 1;

  // tableLoadingFinish(offset / 10 + 1)
  // jesli offset + limit 
  // table.isLoading = true;
  // tableLoadingFinish()
};

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
        toast.error(text)
      }

      else{
        const text = await response.text();

        toast.success("Dodano temat")
        let state = "PENDING"
        if(atob(jsCookie.get('ROLE')) == 'ROLE_ADMIN' || atob(jsCookie.get('ROLE')) == 'ROLE_REDACTOR')
          state = "ACCEPTED"

        data.push({
          id: 0,
          authorName: 'user',
          title: newTopic,
          dateOfUpdate: new Date().toDateString(),
          acceptance: state,})
      }
  } catch (error) {
      console.log(error);
  }


}


</script>

<style>
@import '../../assets/userLists.css';
.vtl-paging-info.col-sm-12.col-md-4,
.vtl-paging-change-div.col-sm-12.col-md-4,
.vtl-paging-pagination-ul.vtl-pagination li:first-child,
.vtl-paging-pagination-ul.vtl-pagination li:last-child
{
    display:none;
}
</style>