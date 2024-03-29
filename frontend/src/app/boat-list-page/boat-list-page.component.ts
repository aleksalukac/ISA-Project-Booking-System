import { Component, OnInit } from '@angular/core';
import { BoatService } from '../service/boat.service';
import { Boat } from '../model/boat';

@Component({
  selector: 'app-boat-list-page',
  templateUrl: './boat-list-page.component.html',
  styleUrls: ['./boat-list-page.component.css']
})
export class BoatListPageComponent implements OnInit {

  boats:Boat[];
  role:any;
  visiable_sort_button:boolean;

  constructor(private boatService:BoatService) {
    this.boats=[];
   }

  ngOnInit(): void {
    this.getBoats();
    this.role=sessionStorage.getItem('role');
    if(this.role=='Client'){
      this.visiable_sort_button=true;
    }else{
      this.visiable_sort_button=false;
    }
  }

  getBoats(){
    this.boatService.getBoats()
    .subscribe(res=>this.boats=res)
  }
  findBoatByMotorMuber(motorNumber:number){
    this.boatService.findBoatByMotorNumber(motorNumber)
    .subscribe(res=>this.boats=res);
  }
  findBoatByMotorPower(motorPower:number){
    this.boatService.findBoatByMotorPower(motorPower)
    .subscribe(res=>this.boats=res);
  }
  sortByName(){
    this.boatService.sortByName()
    .subscribe(res=>this.boats=res)
  }
  sortByGrade(){
    this.boatService.sortByGrade()
    .subscribe(res=>this.boats=res)
  }
  sortByCity(){
    this.boatService.sortByCity()
    .subscribe(res=>this.boats=res)
  }
  findBoatByMotorPowerAndMotorNumber(motorPower:number,motorNumber:number){
    
  }
  findByName(name:string){
    this.boatService.findByName(name)
    .subscribe(res=>this.boats=res)
  }
  findByCity(city:string){
    this.boatService.findByCity(city)
    .subscribe(res=>this.boats=res)
  }

}
