<template>
  <div class="properties">
    <div class="input-add">
      <button @click="addTopic">Dodaj użytkownika</button>
    </div>
    
    <div class="input-look">
      <div class="input-box">
        <div class="select-wrapper">
          <select ref="selectedOption">
            <option value="" selected>--Wybierz opcję--</option>
            <option value="username">Username</option>
            <option value="name">Imie</option>
            <option value="surname">Nazwisko</option>
            <option value="email">Email</option>
          </select>
        </div>
        <div class="input-wrapper">
          <input type="text" v-model="textInput" placeholder="Szukaj">
        </div>
        <button @click="submit">Submit</button>
      </div>
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
import EditImage from "@/assets/icons8-edit.svg";
import DeleteImage from "@/assets/icons8-trash.svg";
const selectedOption = ref("");
const textInput = ref("");

const data = reactive([]);
var url = '/editorial/proposal?';
var page = 0;
var size = 10;

function editUser() {
  let id=this.getAttribute("rowId");

  console.log("Edit data with id:"+ id);
}

function deleteUser() {
  let id=this.getAttribute("rowId");
  console.log("Delete data with id:", id);
}

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
    else {
      console.log(response)
      // const responseJson = await response.json();
      // for (let i = 0; i < responseJson.length; i++) {
      //   data.push({
      //     id: responseJson[i]["id"],
      //     authorName: responseJson[i]["authorName"],
      //     title: responseJson[i]["title"],
      //     dateOfUpdate: responseJson[i]["dateOfUpdate"],
      //     state: responseJson[i]["state"],
      //     acceptance: responseJson[i]["state"],
      //   });
      // }
    }
  } catch (error) {
    console.log(error);
  }
}

for (let i = 0; i < 126; i++) {
  data.push({
      id: i,
      user: ""+i,
      topic: "TEST" + i,
      date: new Date().toDateString(),
      state: "approved",
  });
}

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

// fetchProposals();

// Table config
const table = reactive({
  columns: [
      {
          label: "Username",
          field: "user",
          width: "1%",
          sortable: true,
      },
      {
          label: "Imie",
          field: "name",
          width: "5%",
          sortable: true,
          display: function (row) {
              return '<span><a href="#" class="topic" topicId="'+row.id+'">'+row.title+'</a></span>'
          },
      },
      {
          label: "Nazwisko",
          field: "surname",
          width: "1%",
          sortable: true,
      },
      {
          label: "Email",
          field: "email",
          width: "1%",
          sortable: true,
      },
      {
          label: "Hasło",
          field: "password",
          width: "1%",
          sortable: false,
      },
      {
          label: "Actions",
          field: "actions",
          width: "1%",
          sortable: false,
          display: function (row) {
              return `
              <button class="editButton" rowId=${row.id}>
                <img src="${EditImage}" alt="Edit" />
      </button>
      <button class="deleteButton" rowId=${row.id}>
        <img src="${DeleteImage}" alt="Delete" />
      </button>
  `;
          },
      },
  ],
  rows: computed(() => {
      return data.filter(
      (x) =>
          x.user.toLowerCase().includes(searchTerm.value.toLowerCase()) ||
          x.topic.toLowerCase().includes(searchTerm.value.toLowerCase()) ||
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

function addListeners(className, listenerFunction) {
  let elements = document.getElementsByClassName(className);

  Array.prototype.forEach.call(elements, function (element) {
    //checking if cell already has onclick assigned
    if (element.getAttribute('listener') !== 'true') {
      element.setAttribute('listener', 'true');
      element.addEventListener("click", listenerFunction);
    }
  });
}

const tableLoadingFinish = () => {
  table.isLoading = false;
  // addListeners("topic", changeTopicListener);
  if (jsCookie.get('role') == 'admin' || jsCookie.get('role') == 'redactor')
      addListeners("state", changeStateListener);
  
    addListeners("deleteButton", deleteUser);
    addListeners("editButton", editUser);
};

// function changeTopicListener() {
//   let newTopic = prompt("Podaj nowy temat");

//   //TODO: make request instead of local change
//   if (newTopic != null && newTopic !== '') {
//       let id = this.getAttribute('topicId');
//       data[id].topic = newTopic;
//       data[id].date = new Date().toDateString();
//       data[id].state = "proposed";
//   }
//   tableLoadingFinish();
// }

// function changeStateListener() {
//   //TODO: make request instead of local change
//   let id = this.getAttribute('topicId');
//   let currentState = data[id].state;

//   if (currentState == 'proposed')
//       data[id].state = 'approved';
//   else if (currentState == 'approved')
//       data[id].state = 'rejected';
//   else
//       data[id].state = 'proposed';

//   tableLoadingFinish();
// }



// const addTopic = async () => {
//   const newTopic = newTopicProposal.value;
//   const request = {
//       title: newTopic,
//   };

//   console.log(document.cookie);

//   // try {
//   //     const url = '/editorial/proposal';

//   //     const response = await fetch(url, {
//   //     method: 'POST',
//   //     credentials: 'include',
//   //     headers: {
//   //         'Content-Type': 'application/json',
//   //     },
//   //     body: JSON.stringify(request),
//   //     });

//   //     if (!response.ok) {
//   //       const text = await response.text();
//   //       toast.error(text)

//   //     }
//   //     else{
//   //       const text = await response.text();

//   //       toast.success("Dodano temat")
//   //     }
//   // } catch (error) {
//   //     console.log(error);
//   // }

//   data.push({
//     id: maxId.value++,
//     user: 'user',
//     topic: newTopicProposal.value,
//     date: new Date().toDateString(),
//     state: 'proposed',
//   });
//   newTopicProposal.value = "";
// }



</script>

<style>
@import '../../assets/userLists.css';
.input-box {
  display: flex;
  align-items: center;
  border:none;
  padding: 8px;
}

.select-wrapper,
.input-wrapper {
  flex: 1;
}

.select-wrapper select,
.input-wrapper input {
  width: 100%;
  border: solid 1px;
  padding: 8px;
  border-radius: 4px;
}
</style>
