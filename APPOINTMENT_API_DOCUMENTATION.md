# PetMate Appointment System API Documentation

## Overview
This document describes the complete API endpoints for the PetMate appointment system, allowing users to book appointments with veterinarians and doctors to manage their schedules.

## Base URL
```
http://localhost:8080/api
```

## Authentication
All endpoints require authentication via headers:
- `User-Id`: The ID of the authenticated user/doctor
- `User-Role`: The role of the user (USER, DOCTOR, ADMIN)

## Appointment Endpoints

### 1. Create Appointment
**POST** `/appointments`

Creates a new appointment for a user with a selected doctor.

**Headers:**
```
User-Id: {userId}
Content-Type: application/json
```

**Request Body:**
```json
{
  "doctorId": "doctor123",
  "date": "2024-01-15",
  "time": "14:30:00",
  "symptoms": "My dog is not eating and seems lethargic",
  "appointmentType": "IN_CLINIC",
  "petType": "DOG",
  "userContactNumber": "+1234567890"
}
```

**Response:**
```json
{
  "status": true,
  "statusCode": 201,
  "message": "Appointment created successfully",
  "timestamp": "2024-01-10T10:30:00Z",
  "data": {
    "id": "appointment123",
    "doctorId": "doctor123",
    "doctorName": "Dr. Smith",
    "userId": "user123",
    "userName": "John Doe",
    "date": "2024-01-15",
    "time": "14:30:00",
    "symptoms": "My dog is not eating and seems lethargic",
    "status": "PENDING",
    "appointmentType": "IN_CLINIC",
    "petType": "DOG",
    "userContactNumber": "+1234567890",
    "notes": null,
    "createdAt": "2024-01-10",
    "updatedAt": "2024-01-10"
  }
}
```

### 2. Get User Appointments
**GET** `/appointments/user/{userId}`

Retrieves all appointments for a specific user.

**Response:**
```json
{
  "status": true,
  "statusCode": 200,
  "message": "User appointments retrieved successfully",
  "timestamp": "2024-01-10T10:30:00Z",
  "data": [
    {
      "id": "appointment123",
      "doctorId": "doctor123",
      "doctorName": "Dr. Smith",
      "userId": "user123",
      "userName": "John Doe",
      "date": "2024-01-15",
      "time": "14:30:00",
      "symptoms": "My dog is not eating and seems lethargic",
      "status": "PENDING",
      "appointmentType": "IN_CLINIC",
      "petType": "DOG",
      "userContactNumber": "+1234567890",
      "notes": null,
      "createdAt": "2024-01-10",
      "updatedAt": "2024-01-10"
    }
  ]
}
```

### 3. Get Doctor Appointments
**GET** `/appointments/doctor/{doctorId}`

Retrieves all appointments for a specific doctor.

**Response:** Same structure as user appointments.

### 4. Get Appointment by ID
**GET** `/appointments/{appointmentId}`

Retrieves a specific appointment by its ID.

**Headers:**
```
User-Id: {userId}
User-Role: {userRole}
```

### 5. Update Appointment Status
**PUT** `/appointments/{appointmentId}/status`

Allows doctors to update the status of an appointment.

**Headers:**
```
User-Id: {doctorId}
Content-Type: application/json
```

**Request Body:**
```json
{
  "status": "CONFIRMED",
  "notes": "Appointment confirmed. Please arrive 10 minutes early."
}
```

**Available Statuses:**
- `PENDING` - Initial status when appointment is created
- `CONFIRMED` - Doctor has confirmed the appointment
- `CANCELLED` - Appointment has been cancelled
- `COMPLETED` - Appointment has been completed

### 6. Get Appointments by Status
**GET** `/appointments/status/{status}`

Retrieves appointments filtered by status for the authenticated user.

**Headers:**
```
User-Id: {userId}
User-Role: {userRole}
```

**Path Variables:**
- `status`: One of PENDING, CONFIRMED, CANCELLED, COMPLETED

### 7. Get Doctor Appointments by Date
**GET** `/appointments/doctor/{doctorId}/date/{date}`

Retrieves all appointments for a doctor on a specific date.

**Path Variables:**
- `doctorId`: The doctor's ID
- `date`: Date in ISO format (YYYY-MM-DD)

## Dashboard Endpoints

### 1. User Dashboard
**GET** `/dashboard/user/{userId}`

Provides comprehensive dashboard data for users including appointment summaries and recent appointments.

**Response:**
```json
{
  "status": true,
  "statusCode": 200,
  "message": "User dashboard data retrieved successfully",
  "timestamp": "2024-01-10T10:30:00Z",
  "data": {
    "summary": {
      "totalAppointments": 5,
      "pendingAppointments": 2,
      "confirmedAppointments": 2,
      "completedAppointments": 1
    },
    "recentAppointments": [...],
    "pendingAppointments": [...],
    "confirmedAppointments": [...]
  }
}
```

### 2. Doctor Dashboard
**GET** `/dashboard/doctor/{doctorId}`

Provides comprehensive dashboard data for doctors including appointment summaries and recent appointments.

### 3. User Upcoming Appointments
**GET** `/dashboard/user/{userId}/upcoming`

Retrieves all upcoming appointments (pending or confirmed) for a user.

### 4. Doctor Today's Appointments
**GET** `/dashboard/doctor/{doctorId}/today`

Retrieves all appointments for a doctor on the current date.

## Data Models

### Appointment Status Enum
```java
public enum AppointmentStatus {
    PENDING,      // Initial status
    CONFIRMED,    // Doctor confirmed
    CANCELLED,    // Cancelled
    COMPLETED     // Finished
}
```

### Appointment Type Enum
```java
public enum AppointmentType {
    IN_CLINIC,    // Visit to clinic
    HOME_VISIT    // Doctor visits home
}
```

### Pet Type Enum
```java
public enum PetType {
    DOG, CAT, BIRD, FISH, RABBIT, HAMSTER, OTHER
}
```

## Validation Rules

### Appointment Creation
- Date cannot be in the past
- Time must be between 8:00 AM and 8:00 PM
- Date cannot be more than 3 months in the future
- Appointments cannot be scheduled on weekends
- All required fields must be provided
- No conflicting appointments for the same doctor at the same time

### Status Updates
- Only the assigned doctor can update appointment status
- Users cannot modify appointment details after creation
- Status can only be changed to valid enum values

## Error Handling

### Common Error Responses

**400 Bad Request:**
```json
{
  "status": false,
  "statusCode": 400,
  "message": "Validation error",
  "timestamp": "2024-01-10T10:30:00Z",
  "data": null
}
```

**404 Not Found:**
```json
{
  "status": false,
  "statusCode": 404,
  "message": "Appointment not found",
  "timestamp": "2024-01-10T10:30:00Z",
  "data": null
}
```

**409 Conflict:**
```json
{
  "status": false,
  "statusCode": 409,
  "message": "Doctor has a conflicting appointment at this time",
  "timestamp": "2024-01-10T10:30:00Z",
  "data": null
}
```

**403 Forbidden:**
```json
{
  "status": false,
  "statusCode": 403,
  "message": "You don't have access to this appointment",
  "timestamp": "2024-01-10T10:30:00Z",
  "data": null
}
```

## Usage Examples

### Frontend Integration

**Creating an Appointment:**
```javascript
const createAppointment = async (appointmentData) => {
  const response = await fetch('/api/appointments', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'User-Id': userId
    },
    body: JSON.stringify(appointmentData)
  });
  return response.json();
};
```

**Getting User Dashboard:**
```javascript
const getUserDashboard = async (userId) => {
  const response = await fetch(`/api/dashboard/user/${userId}`);
  return response.json();
};
```

**Updating Appointment Status (Doctor):**
```javascript
const updateAppointmentStatus = async (appointmentId, status, notes) => {
  const response = await fetch(`/api/appointments/${appointmentId}/status`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      'User-Id': doctorId
    },
    body: JSON.stringify({ status, notes })
  });
  return response.json();
};
```

## Security Considerations

1. **Authentication Required**: All endpoints require valid user authentication
2. **Authorization**: Users can only access their own appointments, doctors can only manage their assigned appointments
3. **Input Validation**: All input is validated for security and business logic
4. **CORS**: Cross-origin requests are enabled for frontend integration
5. **Rate Limiting**: Consider implementing rate limiting for production use

## Testing

Use the provided Postman collection or test with tools like:
- Postman
- cURL
- Insomnia
- Frontend applications

## Support

For technical support or questions about the API, please refer to the backend development team or create an issue in the project repository.


