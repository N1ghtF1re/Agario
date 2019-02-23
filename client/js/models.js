"use strict"

class Person {
   constructor(person_from_json, socket) {
      this.id = person_from_json.id
      this.username = person_from_json.username
      this.center = person_from_json.center
      this.color = person_from_json.color
      this.size = person_from_json.size
      this.dirrection = DIR_RIGHT
   }

   equals(person) {
      return person.id === this.id
   }

   specialDraw(ctx) { // Отрисовка владельца очереди
      this.draw(ctx)
      ctx.fillStyle = "#cf00ff"
      ctx.beginPath()
      ctx.arc(this.center.x, this.center.y, this.size+2, 0, 2 * Math.PI);
      ctx.stroke()
   }

   draw(ctx) {
      ctx.fillStyle = this.color
      ctx.beginPath()
      ctx.arc(this.center.x, this.center.y, this.size, 0, 2 * Math.PI);
      ctx.fill()

      if(this.username == "mob") return

      ctx.fillStyle = "#000"
      let fontSize = 15*this.size/20
      ctx.font = fontSize + "px Arial";
      ctx.textAlign = "center";
      ctx.fillText(this.username, this.center.x, this.center.y+this.size+fontSize);
   }
}

class PersonsList {
   constructor() {
      this.persons = []
   }

   add(person) {
      this.persons.push(person)
   }

   getArrayIndex(person) {
      for(let i = 0; i < this.persons.length; i++) {
         if(this.persons[i].equals(person)) {
            return i;
         }
      }
   }

   remove(person) {
      let index = this.getArrayIndex(person)
      this.persons.splice(index,1)
   }

   change(person) {
      let index = this.getArrayIndex(person)
      this.persons[index].center = person.center
   }

   resize(person) {
      let index = this.getArrayIndex(person)
      this.persons[index].size = person.size
   }

   toArray() {
      return this.persons
   }
}
