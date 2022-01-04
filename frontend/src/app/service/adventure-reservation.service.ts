import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { AdventureReservation } from '../model/AdventureReservation';
import { AdventureFastReservation } from '../model/adventureFastReservation';

@Injectable({
  providedIn: 'root'
})
export class AdventureReservationService {
  url = "http://localhost:8090/api/adventure-reservations";
  urlReservation = "http://localhost:8090/api/adventureReservation";
  constructor(private http: HttpClient) { }

  activeReservations(id: number): Observable<AdventureReservation[]> {
    return this.http.get<AdventureReservation[]>(this.url + "/active/" + id);
  }
  sortByPrice(id: number): Observable<AdventureReservation[]> {
    return this.http.get<AdventureReservation[]>(this.url + "/sort-by-price/" + id);
  }
  sortByDate(id: number): Observable<AdventureReservation[]> {
    return this.http.get<AdventureReservation[]>(this.url + "/sort-by-date/" + id);
  }
  saveFastReservation(reservation: AdventureFastReservation): Observable<AdventureFastReservation> {
    return this.http.put<AdventureFastReservation>(`${this.urlReservation}` + '/addFastReservation', reservation);
  }
  saveReservation(reservation: AdventureReservation): Observable<AdventureReservation> {
    return this.http.put<AdventureReservation>(`${this.urlReservation}` + '/addReservation', reservation);
  }
}
