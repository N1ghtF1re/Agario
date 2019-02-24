"use strict"
// CONSTANTS:
const DIR_LEFT = 0
const DIR_RIGHT = 1
const DIR_UP = 2
const DIR_DOWN = 3

const SPEED = 3
const DELAY = 80

// VARS

var username = prompt("Enter username")
var socket = new WebSocket("ws://localhost:8080/agario/" + username);

socket.onmessage = onMessage;
socket.onclose = onClose;

var field = document.getElementById("game-field")
var ctx = field.getContext('2d');

var timer

var persons = new PersonsList() // Список персонажей
var my_person = null // Персонаж текущего пользователя

document.onkeydown = function(e) {
   switch (e.key) {
      case "ArrowUp":
         my_person.dirrection = DIR_UP
         break
      case "ArrowDown":
         my_person.dirrection = DIR_DOWN
         break
      case "ArrowLeft":
         my_person.dirrection = DIR_LEFT
         break
      case "ArrowRight":
         my_person.dirrection = DIR_RIGHT
         break
   }
}

function redraw() {
   ctx.clearRect(0, 0, field.width, field.height)
   persons.toArray().forEach(function(item) {
      if(item.equals(my_person)) {
         item.specialDraw(ctx)
      } else {
         item.draw(ctx)
      }
   })
}

function move() {
   switch(my_person.dirrection) {
      case DIR_LEFT:
         if(my_person.center.x <= 0) break
         my_person.center.x -= SPEED
         break;
      case DIR_RIGHT:
         if(my_person.center.x >= field.width) break
         my_person.center.x += SPEED
         break;
      case DIR_UP:
         if(my_person.center.y <= 0) break
         my_person.center.y -= SPEED
         break;
      case DIR_DOWN:
         if(my_person.center.y >= field.height) break
         my_person.center.y += SPEED
         break;
   }
   redraw()
   let msg = {"changingType": "COORDS_CHANGING", "person": my_person}
   socket.send(JSON.stringify(msg))
}

function onMessage(event) {
    let message = JSON.parse(event.data);
    let person = new Person(message.person, socket)

    switch (message.changingType) {
      case "MY_SPAWN":
         my_person = person
         timer = new Timer(move, DELAY);
         timer.start()
         // break опущен тк нужно продолжить спавн :)
      case "SPAWN":
         persons.add(person)
         break
      case "DEAD":
         console.log(message)
         persons.remove(person) // Удаляем из списка убитого персонажа
         if(person.equals(my_person)) {
            timer.stop()
            alert("You lose")
         }
         break
      case "COORDS_CHANGING":
         persons.change(person)
         break
      case "SIZE_CHANGING":
         persons.resize(person)
         break
      default:
         console.log(message)
         break
    }
    redraw()
}

function onClose() {
   alert("Server die")
   timer.stop()
}
