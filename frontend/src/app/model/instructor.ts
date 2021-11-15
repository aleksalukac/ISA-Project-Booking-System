export interface InstructorInterface {
    id?: number;
    firstName: string;
    lastName: string;
    username: string;
    password: string;
    address: string;
    state: string;
    city: string;
    email: string;
    mobile: string;


}
export class Instructor implements InstructorInterface {
    id?: number | undefined;
    firstName: string;
    lastName: string;
    username: string;
    password: string;
    address: string;
    state: string;
    city: string;
    email: string;
    mobile: string;
    constructor(obj: InstructorInterface) {
        this.id = obj.id;
        this.firstName = obj.firstName;
        this.lastName = obj.lastName;
        this.username = obj.username
        this.password = obj.password;
        this.address = obj.address;
        this.city = obj.city;
        this.state = obj.state;
        this.email = obj.email;
        this.mobile = obj.mobile;

    }


}
