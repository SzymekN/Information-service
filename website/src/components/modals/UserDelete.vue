<template>
    <div class="user-modal">
      <div class="modal-content">
        <div class="heading">
          <h2>Usuwanie użytkownika</h2>        </div>
        <p>Czy na pewno chcesz usunąć użytkownika <b>{{ user }}</b>?</p>
        <div class="buttons">
            <button @click="$emit('close')" class="cancel-button" >Anuluj</button>
            <button @click="$emit('submit')" class="confirm-button">Usuń</button>         
        </div>
      </div>
    </div>
  </template>
  
  <script>
export default {
  props: {
    user: {
      type: String,
      required: true
    }
  }
};
</script>
  
  <style scoped>
  b{
    font-weight: bold;
  }
  .user-modal {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: rgba(0, 0, 0, 0.5);
    z-index: 999;
  }
  
  .modal-content {
    background-color: white;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
    width: 30%;
  }
  p{
    margin:0 0 15px 0;
  }
  .heading {
    height: 50px;
  }
  
  .buttons {
    display: flex;
    justify-content: flex-end;
  }
  button{
    color: white;
    border: solid 1px black;
    border-radius: 4px;
    cursor: pointer;
  }
  .confirm-button {
    padding: 8px 16px;
    margin-left: 10px;
    background-color: red;
  }
  .confirm-button:hover {
    background-color: darkred;
  }
  .cancel-button {
    background-color: grey;
  }
  .cancel-button:hover {
    background-color: darkgrey;
  }
  
  h2 {
    margin: 0;
  }
  </style>
  