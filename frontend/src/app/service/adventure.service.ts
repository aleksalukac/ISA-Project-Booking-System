import { AdditionalItem } from './../model/additionalItem';
import { AdventureBehavioralRules } from './../model/adventureBehavioralRules';
import { AdventureFishingEquipment } from './../model/adventureFishingEquipment';
import { HttpClient, HttpClientModule, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Adventure } from '../model/adventure';
import { AdventureFastReservation } from '../model/adventureFastReservation';

@Injectable({
  providedIn: 'root'
})
export class AdventureService {
  urlAdventures = "http://localhost:8090/api/adventures";
  urlAdventure = "http://localhost:8090/api/instructor/adventure";

  constructor(private http: HttpClient) { }

  getAdventures(): Observable<Adventure[]> {
    return this.http.get<Adventure[]>(this.urlAdventures);
  }
  getAdventure(id: number): Observable<Adventure> {
    return this.http.get<Adventure>(`${this.urlAdventures}/${id}`);
  }
  deleteAdventure(id: number) {
    return this.http.delete(`${this.urlAdventures}/` + `delete` + `/${id}`);
  }
  getAdventureEquipment(id: number): Observable<AdventureFishingEquipment[]> {
    return this.http.get<AdventureFishingEquipment[]>(`${this.urlAdventure}/` + `equipment` + `/${id}`);
  }

  getAdventureBehavioralRules(id: number): Observable<AdventureBehavioralRules[]> {
    return this.http.get<AdventureBehavioralRules[]>(`${this.urlAdventure}/` + `rules` + `/${id}`);
  }
  getAdventureAdditionalItems(id: number): Observable<AdditionalItem[]> {
    return this.http.get<AdditionalItem[]>(`${this.urlAdventure}/` + `additionalItems` + `/${id}`);
  }
  getAdventureFastReservations(id: number): Observable<AdventureFastReservation[]> {
    return this.http.get<AdventureFastReservation[]>(`${this.urlAdventure}/` + `fastReservations` + `/${id}`);
  }

  updateAdvenuture(id: number, data: Adventure): Observable<Adventure> {
    return this.http.post<Adventure>(`${this.urlAdventure}/${id}`, data);
  }
  addAdventure(id: number, newAdventure: Adventure): Observable<Adventure> {
    return this.http.put<Adventure>(`${this.urlAdventures}/` + `add` + `/${id}`, newAdventure);
  }

  saveAdventureEquipment(id: number, data: AdventureFishingEquipment): Observable<AdventureFishingEquipment> {
    return this.http.post<AdventureFishingEquipment>(`${this.urlAdventure}/` + `equipment` + `/${id}`, data);
  }
  updateAdvenutureEquipment(id: number, data: AdventureFishingEquipment): Observable<AdventureFishingEquipment> {
    return this.http.post<AdventureFishingEquipment>(`${this.urlAdventure}/` + `equipmentEdit` + `/${id}`, data);
  }
  deleteAdventureEquipment(adventureId: number, equipmentId: number): Observable<AdventureFishingEquipment> {
    return this.http.post<AdventureFishingEquipment>(`${this.urlAdventure}/${adventureId}/${equipmentId}`, null);
  }

  findByInstructorFirstAndLastName(firstName: string, lastName: string): Observable<Adventure[]> {
    const params = new HttpParams()
      .set('firstName', firstName)
      .set('lastName', lastName);
    return this.http.get<Adventure[]>(this.urlAdventures, { params });
  }
  findByInstructor(instructorId: number): Observable<Adventure[]> {
    const params: HttpParams = new HttpParams().append('instructorId', instructorId);
    return this.http.get<Adventure[]>(this.urlAdventures, { params });
  }
  findByName(name: string): Observable<Adventure[]> {
    const params: HttpParams = new HttpParams().append('name', name);
    return this.http.get<Adventure[]>(this.urlAdventures, { params });
  }
  findByCity(city: string): Observable<Adventure[]> {
    const params: HttpParams = new HttpParams().append('city', city);
    return this.http.get<Adventure[]>(this.urlAdventures, { params });
  }
}