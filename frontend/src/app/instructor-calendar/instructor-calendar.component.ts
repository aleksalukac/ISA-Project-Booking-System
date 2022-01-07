import { AdventureFastReservation } from './../model/adventureFastReservation';
import { AnalyticsService } from './../service/analytics.service';
import { InstructorService } from './../service/instructor.service';
import {
  Component,
  ChangeDetectionStrategy,
  OnInit,
  Input,
  ViewChild,
  TemplateRef,

} from '@angular/core';
import {
  startOfDay,
  endOfDay,
  subDays,
  addDays,
  endOfMonth,
  isSameDay,
  isSameMonth,
  addHours,
  subMonths,
} from 'date-fns';
import { Subject } from 'rxjs';
import {
  CalendarEvent,
  CalendarEventAction,
  CalendarEventTimesChangedEvent,
  CalendarView,
} from 'angular-calendar';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, FormGroup } from '@angular/forms';
import { TimePeriod } from '../model/timePeriod';
import { UnavailabilityType } from '../model/unavailabilityType';
import { AdventureReservation } from '../model/AdventureReservation';
import { ThrowStmt } from '@angular/compiler';

const colors: any = {
  red: {
    primary: '#ad2121',
    secondary: '#FAE3E3',
  },
  blue: {
    primary: '#1e90ff',
    secondary: '#D1E8FF',
  },
  yellow: {
    primary: '#e3bc08',
    secondary: '#FDF1BA',
  },
  green: {
    primary: '#5DE174',
    secondary: '#BEF6C8',
  }
};

@Component({
  selector: 'app-instructor-calendar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  styleUrls: ['./instructor-calendar.component.css'],
  templateUrl: './instructor-calendar.component.html',
})
export class InstructorCalendarComponent implements OnInit {
  @Input() id: number;
  @ViewChild('modalContent', { static: true }) modalContent: TemplateRef<any>;
  @ViewChild('modalContentAction', { static: true }) modalContentAction: TemplateRef<any>;
  startTime: any;
  endTime: any;
  unvailabilities: TimePeriod[];
  period: TimePeriod = new TimePeriod({
    start: '',
    end: '',
    type: UnavailabilityType.Default
  })
  showReport: boolean = false;
  view: CalendarView = CalendarView.Month;
  newEvent: CalendarEvent;
  CalendarView = CalendarView;
  formValue!: FormGroup;
  viewDate: Date = new Date();
  modalData: {
    action: string;
    event: CalendarEvent;
  };

  actions: CalendarEventAction[] = [
    {
      label: '<i class="fa fa-fa-pencil-alt"></i>',
      a11yLabel: 'Edit',
      onClick: ({ event }: { event: CalendarEvent }): void => {
        this.handleEvent('Edited', event);
      },
    },
    {
      label: '<i class="fas fa-fw fa-trash-alt"></i>',
      a11yLabel: 'Delete',
      onClick: ({ event }: { event: CalendarEvent }): void => {
        this.events = this.events.filter((iEvent) => iEvent !== event);
        this.handleEvent('Deleted', event);
      },
    },
  ];

  refresh: Subject<any> = new Subject();

  events: CalendarEvent[] = [


  ];

  activeDayIsOpen: boolean = true;
  reservations: AdventureReservation[];
  fastReservations: AdventureFastReservation[];
  selectedReservation: AdventureReservation;
  selectedFastReservation: AdventureFastReservation;
  constructor(private modal: NgbModal, private formBuilder: FormBuilder, private instructorService: InstructorService, private analyticsService: AnalyticsService) { }
  ngOnInit(): void {
    this.formValue = this.formBuilder.group({
      startTime: [''],
      endTime: ['']
    })
    this.getUnavailability();
    this.getInstructorReservations();
    this.getInstructorFastReservations();
  }

  getInstructorReservations() {
    this.analyticsService.getInstructorReservations(this.id)
      .subscribe(res => {
        this.reservations = res;
      })
  }

  getInstructorFastReservations() {
    this.instructorService.getInstructorFastReservations(this.id)
      .subscribe(res => this.fastReservations = res)
  }

  getUnavailability() {
    this.instructorService.getUnavailabilityByInstructor(this.id)
      .subscribe(res => {
        this.unvailabilities = res
        this.set();
      })
  }

  set() {
    this.unvailabilities.forEach((u, index) => {

      this.newEvent = {
        start: new Date(u.start),
        end: new Date(u.end),
        title: u.type.toString(),
        color: colors.red,
        actions: this.actions,
        resizable: {
          beforeStart: true,
          afterEnd: true,
        },
        draggable: true,
      }
      if (u.type == UnavailabilityType.Action)
        this.newEvent.color = colors.yellow;
      else if (u.type == UnavailabilityType.Reservation)
        this.newEvent.color = colors.blue;

      this.events.push(this.newEvent);
    });
  }

  setUnavailability() {
    this.period.start = this.startTime.toLocaleString();
    this.period.end = this.endTime.toLocaleString()
    this.instructorService.setUnavailability(this.period, this.id).subscribe(data => {
      this.newEvent = {
        start: new Date(this.startTime),
        end: new Date(this.endTime),
        title: 'Unavailable',
        color: colors.red,
        actions: this.actions,
        resizable: {
          beforeStart: true,
          afterEnd: true,
        },
        draggable: true,
      }
      this.events.push(this.newEvent);

    }, error => {
      alert("The selected time period overlaps with the previously entered one! Please choose another one!")
    })
  }

  dayClicked({ date, events }: { date: Date; events: CalendarEvent[] }): void {
    if (isSameMonth(date, this.viewDate)) {
      if (
        (isSameDay(this.viewDate, date) && this.activeDayIsOpen === true) ||
        events.length === 0
      ) {
        this.activeDayIsOpen = false;
      } else {
        this.activeDayIsOpen = true;
      }
      this.viewDate = date;
    }
  }

  eventTimesChanged({
    event,
    newStart,
    newEnd,
  }: CalendarEventTimesChangedEvent): void {
    this.events = this.events.map((iEvent) => {
      if (iEvent === event) {
        return {
          ...event,
          start: newStart,
          end: newEnd,
        };
      }
      return iEvent;
    });
    this.handleEvent('Dropped or resized', event);
  }

  handleEvent(action: string, event: CalendarEvent): void {
    this.modalData = { event, action };
    this.reservations.forEach((u, index) => {

      if (new Date(u.reservationStart).toDateString() == event.start.toDateString()) {
        this.selectedReservation = u;
        this.showReport = false;
        if (new Date() > new Date(u.reservationEnd)) {
          this.showReport = true;
        }

        this.modal.open(this.modalContent, { size: 'md' });
      }
    })
    this.fastReservations.forEach((u, index) => {

      if (new Date(u.reservationStart).toDateString() == event.start.toDateString()) {
        this.selectedFastReservation = u;
        this.modal.open(this.modalContentAction, { size: 'md' });

      }
    })


  }

  addEvent(): void {
    this.events = [
      ...this.events,
      {
        title: 'New event',
        start: startOfDay(new Date()),
        end: endOfDay(new Date()),
        color: colors.red,
        draggable: true,
        resizable: {
          beforeStart: true,
          afterEnd: true,
        },
      },
    ];
  }

  deleteEvent(eventToDelete: CalendarEvent) {
    this.events = this.events.filter((event) => event !== eventToDelete);
  }

  setView(view: CalendarView) {
    this.view = view;
  }

  closeOpenMonthViewDay() {
    this.activeDayIsOpen = false;
  }

  addReport() {
    sessionStorage.setItem("adventureReservation", JSON.stringify(this.selectedReservation));
    let ref = document.getElementById('closeOK');
    ref?.click();
  }
}