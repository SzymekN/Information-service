<!-- https://vue3-lite-table.vercel.app/usage -->
<!-- TODO:
    1. User has only possibility to change own topics
    2. Change fake data to fetching from database
    3. As admin/redactor possibility to change all topics, approve or reject
-->

<template>
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
        :is-static-mode="true"
        :columns="table.columns"
        :rows="table.rows"
        :total="table.totalRecordCount"
        :sortable="table.sortable"
        @is-finished="tableLoadingFinish"
        @row-clicked="tableLoadingFinish"
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
var page = 0;
var size = 10;

const fetchProposals = async () =>{
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
      console.log(response)
      const responseJson = await response.json();
      for (let i = 0; i < responseJson.length; i++) {
        data.push({
          id: responseJson[i]["id"],
          authorName: responseJson[i]["authorName"],
          title: responseJson[i]["title"],
          dateOfUpdate: responseJson[i]["dateOfUpdate"],
          state: responseJson[i]["state"],
          acceptance: responseJson[i]["state"],
        });
      }
    }
  } catch (error) {
    console.log(error);
  }
}

// for (let i = 0; i < 127; i++) {
//   data.push({
//       id: i,
//       user: ""+i,
//       topic: "TEST" + i,
//       date: new Date().toDateString(),
//       state: "approved",
//   });
// }

// data.push({
//   id: 127,
//   user: ""+127,
//   topic: "TEST" + 127,
//   date: (new Date().toDateString()),
//   state: "rejected",
// });

const searchTerm = ref(""); // Search text
const newTopicProposal = ref(""); // user input with proposition
const maxId = ref(128); // last id assigned
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
              if (row.state == "proposed")
                  color = "#e8b53f";
              if (row.state == "approved")
                  color = "#05a32f";
              if (row.state == "rejected")
                  color = "#a31505";
              
              //make state clickable if user is admin or redactor
              if (jsCookie.get('role') == 'admin' || jsCookie.get('role') == 'redactor')
                  return '<span><a href="#" style=color:'+color+' class="state" topicId="'+row.id+'">'+row.acceptance+'</a></span>'
              return '<span style=color:'+color+'>'+row.acceptance+'</span>'
          },
      },
  ],
  rows: computed(() => {
      return data.filter(
      (x) =>
          x.authorName.toLowerCase().includes(searchTerm.value.toLowerCase()) ||
          x.title.toLowerCase().includes(searchTerm.value.toLowerCase()) ||
          x.acceptance.toLowerCase().includes(searchTerm.value.toLowerCase()) ||
          x.dateOfUpdate.toLowerCase().includes(searchTerm.value.toLowerCase())
      );
  }),
  totalRecordCount: computed(() => {
      return table.rows.length;
  }),
  sortable: {
      order: "id",
      sort: "asc",
  },
});

function changeTopicListener(){
  let newTopic = prompt("Podaj nowy temat")

  //TODO: make request instead of local change
  if (newTopic != null && newTopic !== ''){
      let id = this.getAttribute('topicId');
      data[id].topic = newTopic;
      data[id].date = new Date().toDateString();
      data[id].state = "proposed";
  }
  tableLoadingFinish()
}

function changeStateListener(){

  //TODO: make request instead of local change
  let id = this.getAttribute('topicId');
  let currentState = data[id].state;

  if (currentState == 'proposed')
      data[id].state = 'approved';
  else if (currentState == 'approved')
      data[id].state = 'rejected';
  else
      data[id].state = 'proposed';


  tableLoadingFinish()
}

function addListeners(className, listenerFunction){
  let elements = document.getElementsByClassName(className)

  Array.prototype.forEach.call(elements, function (element) {
  //checking if cell already has onclick assigned
  if(element.getAttribute('listener') !== 'true'){
      element.setAttribute('listener', 'true');
      element.addEventListener("click", listenerFunction);
  }
  });

}

const tableLoadingFinish = () => {

  table.isLoading = false;
  addListeners("topic", changeTopicListener);
  if (jsCookie.get('role') == 'admin' || jsCookie.get('role') == 'redactor')
      addListeners("state", changeStateListener);

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
      }
  } catch (error) {
      console.log(error);
  }

  data.push({
          id: maxId.value++,
          user: 'user',
          topic: newTopicProposal.value,
          date: new Date().toDateString(),
          state: 'proposed',})
  newTopicProposal.value = "";
}


</script>

<style>
@import '../../assets/userLists.css';
</style>