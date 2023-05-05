<!-- https://vue3-lite-table.vercel.app/usage -->
<!-- TODO:
    1. User has only possibility to change own topics
    2. Change fake data to fetching from database
    3. As admin/redactor possibility to change all topics, approve or reject
-->

<template>
    <div style="text-align: left">
        <label>Szukaj:</label><input v-model="searchTerm" />
    </div>  
    <div style="text-align: left">
        <label>Nowy temat:</label><input v-model="newTopicProposal" />
        <button @click="addTopic">dodaj</button>
    </div>
    <table-lite
        :is-static-mode="false"
        :columns="table.columns"
        :rows="table.rows"
        :total="table.totalRecordCount"
        :sortable="table.sortable"
        @is-finished="tableLoadingFinish"
        @do-search="fetchArticles"
        @row-clicked="tableLoadingFinish"
    ></table-lite>
  </template>
  
  <script setup>
  import jsCookie from "js-cookie";
  import { reactive, ref, computed, onBeforeMount } from "vue";
  import TableLite from 'vue3-table-lite'
import router from "../../router";
  
  // TODO: replace with fetched data
  // Fake Data for 'asc' sortable
  const data = reactive([]);
  
  const fetchArticles = async () => {
    try {
        table.isLoading = true;
        const response = await fetch('http://localhost:8080/client/articles');
        const temp = await response.json();
        console.log(temp);
        for (let i = 0; i < temp.length; i++) {
            data.push({
                id: temp[i]["id"],
                user: temp[i]["authorName"],
                topic: temp[i]["title"],
                date: temp[i]["dateOfSubmission"],
                // state: temp[i]["state"],
                state: "TODO",
            });
        }
    console.log("finished fetching data");
    console.log(data);
    table.isLoading = false;
    console.log(data);
    tableLoadingFinish();

    } catch (error) {
      console.log(error);
    }
  };

  onBeforeMount(fetchArticles);

  const searchTerm = ref(""); // Search text
  const newTopicProposal = ref(""); // user input with proposition
  
  // Table config
  const table = reactive({
    columns: [
        {
            label: "Użytkownik",
            field: "user",
            width: "1%",
            sortable: true,
        },
        {
            label: "Temat",
            field: "topic",
            width: "5%",
            sortable: true,
            display: function (row) {
                return '<span><a href="#" class="topic" topicId="'+row.id+'">'+row.topic+'</a></span>'
            },
        },
        {
            label: "Data ostatniej edycji",
            field: "date",
            width: "1%",
            sortable: true,
        },
        {
            label: "Stan",
            field: "state",
            width: "1%",
            sortable: true,
            display: function (row) {
                let color = "#e8b53f";
                if (row.state == "draft")
                    color = "#e8b53f";
                if (row.state == "approved")
                    color = "#05a32f";
                if (row.state == "rejected")
                    color = "#a31505";
                
                //make state clickable if user is admin or redactor
                if (jsCookie.get('role') == 'admin' || jsCookie.get('role') == 'redactor')
                    return '<span><a href="#" style=color:'+color+' class="state" topicId="'+row.id+'">'+row.state+'</a></span>'
                return '<span style=color:'+color+'>'+row.state+'</span>'
            },
        },
    ],
    rows: computed(() => {
        return data.filter(
        (x) =>
            x.user.toLowerCase().includes(searchTerm.value.toLowerCase()) ||
            x.topic.toLowerCase().includes(searchTerm.value.toLowerCase()) ||
            x.date.toLowerCase().includes(searchTerm.value.toLowerCase()) ||
            x.state.toLowerCase().includes(searchTerm.value.toLowerCase())
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
  
  function editArticleListener(){
    
    console.log("addign listener")
    let id = this.getAttribute('topicId');
    console.log(id);
    sessionStorage.setItem("articleToEdit", JSON.stringify(data[id]))
    router.push({path: '/article-edit', params:{redirected: true} });

  }
  
  function changeStateListener(){
  
    //TODO: make request instead of local change
    let id = this.getAttribute('topicId');
    let currentState = data[id].state;
    console.log(currentState);
    console.log(id);
    console.log(data[id]);
    console.log(this)
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
    console.log("halo")
    if(element.getAttribute('listener') !== 'true'){
        element.setAttribute('listener', 'true');
        element.addEventListener("click", listenerFunction);
    }
    });
  
  }
  
  const tableLoadingFinish = () => {
  
    table.isLoading = false;
    console.log("loading")
    addListeners("topic", editArticleListener);
    if (jsCookie.get('role') == 'admin' || jsCookie.get('role') == 'redactor')
        addListeners("state", changeStateListener);
  
  };
  
  
  </script>
  