import { AdventureReservation } from './../model/AdventureReservation';
import { HttpClient, HttpClientModule, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Adventure } from '../model/adventure';
import { Instructor } from '../model/instructor';
import { Client } from '../model/client';
import { AdventureFastReservation } from '../model/adventureFastReservation';
import { Admin } from '../model/admin';

@Injectable({
    providedIn: 'root'
})
export class AdminService {
    urlAdmin = "http://localhost:8090/api/admin";


    constructor(private http: HttpClient) { }
    getAdmins(): Observable<Admin[]> {
        return this.http.get<Admin[]>(this.urlAdmin);
    }

    getById(id: number): Observable<Admin> {
        return this.http.get<Admin>(`${this.urlAdmin}/${id}`);
    }

    updateAdmin(id: number, editedAdmin: Admin): Observable<Admin> {
        return this.http.put<Admin>(`${this.urlAdmin}/${id}`, editedAdmin);
    }

    changePassword(id: number, newPassword: string): Observable<Admin> {
        return this.http.post<Instructor>(`${this.urlAdmin}/` + `changePassword` + `/${id}`, { newPassword });
    }

}
