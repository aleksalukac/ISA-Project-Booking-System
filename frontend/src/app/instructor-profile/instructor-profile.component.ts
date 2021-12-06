import { InstructorService } from './../service/instructor.service';

import { Component, Input, OnInit } from '@angular/core';
import { Instructor } from '../model/instructor';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-instructor-profile',
  templateUrl: './instructor-profile.component.html',
  styleUrls: ['./instructor-profile.component.css']
})
export class InstructorProfileComponent implements OnInit {
  profileShow: boolean = true;
  profileEdit: boolean = false;
  changePassword: boolean = false;

  newPassword: any;
  passwordConfirm: any;
  formV: FormGroup;
  @Input() instructor: Instructor = new Instructor({
    id: 0,
    username: '',
    password: '',
    firstName: '',
    lastName: '',
    email: '',
    street: '',
    city: '',
    state: '',
    mobile: '',
    biography: ''


  });
  @Input() id: number;

  constructor(private instructorService: InstructorService, private route: ActivatedRoute
    , private router: Router, private fb: FormBuilder) {
  }

  ngOnInit(): void {
    this.formV = this.fb.group({

      newPassword: ['', Validators.required],
      passwordConfirm: ['', Validators.required]

    }

    );
  }
  get registerFormControl() {
    return this.formV.controls;
  }
  edit() {
    this.profileShow = !this.profileShow;
    this.profileEdit = !this.profileEdit;
    this.changePassword = false;

  }
  change() {
    this.profileShow = false;
    this.profileEdit = false;
    this.changePassword = true;
  }

  submit() {
    this.instructorService.updateInstructor(this.id, this.instructor).subscribe(res => {
      this.goToProfilePage();
    })
  }

  submitPassword() {
    if (this.formV.valid) {

      if ((this.newPassword != this.passwordConfirm))
        alert("New and confirmed password don't match!");
      else
        this.instructorService.changePassword(this.id, this.newPassword).
          subscribe(res => {
            alert("password successfully changed!")
            this.goToProfilePage();
          })

    }
    else
      alert("Fill the required fields!");

  }
  goToProfilePage() {
    this.profileShow = true;
    this.profileEdit = false;
    this.changePassword = false;
    this.router.navigate(['/instructor/:id']);
  }
  close() {
    this.goToProfilePage();

  }




}
