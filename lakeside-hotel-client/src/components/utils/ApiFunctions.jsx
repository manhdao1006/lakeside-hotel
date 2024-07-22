/* eslint-disable no-unused-vars */
import axios from "axios";

export const api = axios.create({
  baseURL : "http://localhost:9192"
})

export const getHeader = () => {
  const token = localStorage.getItem("token");
  return {
    Authorization: `Bearer ${token}`,
    "Content-Type": "application/json"
  }
}

// This function add room to the thee database
export async function addRoom(photo, roomType, roomPrice) {
  const formData = new FormData();
  formData.append("photo", photo);
  formData.append("roomType", roomType);
  formData.append("roomPrice", roomPrice);

  const response = await api.post("/api/rooms/add/new-room", formData);
  if (response.status === 201) {
    return true;
  } else {
    return false;
  }
}

// This function gets all room types from the database
export async function getRoomTypes() {
  try {
    const response = await api.get("/api/rooms/room-types");
    return response;
  } catch (error) {
    throw new Error("Error fetching room types");
  }
}

// This function gets all rooms from the database
export async function getAllRooms() {
  try {
    const response = await api.get("/api/rooms/all-rooms");
    return response;
  } catch (error) {
    throw new Error("Error fetching rooms");
  }
}

// This function deletes a room by Id
export async function deleteRoom(roomId) {
  try {
    const response = await api.delete(`/api/rooms/delete/room/${roomId}`);
    return response;
  } catch (error) {
    throw new Error(`Error deleting room ${error.message}`);
  }
}

// This function updates a room by Id
export async function updateRoom(roomId, roomData) {
  const formData = new FormData();
  formData.append("roomType", roomData.roomType);
  formData.append("roomPrice", roomData.roomPrice);
  formData.append("photo", roomData.photo);

  const response = await api.put(`/api/rooms/update/room/${roomId}`, formData);
  return response;
}

// This function gets a room by Id
export async function getRoomById(roomId) {
  try {
    const response = await api.get(`/api/rooms/room/${roomId}`);
    return response;
  } catch (error) {
    throw new Error(`Error fetching room ${error.message}`);
  }
}

// This function saves a new booking to the database
export async function bookRoom(roomId, booking) {
  try {
    const response = await api.post(`/api/bookings/room/${roomId}/add-booking`, booking);
    return response;
  } catch (error) {
    if (error.response && error.response.data) {
      throw new Error(error.response.data);
    } else {
      throw new Error(`Error booking room: ${error.message}`);
    }
  }
}

// This function gets all bookings from the database
export async function getAllBookings() {
  try {
    const response = await api.get("/api/bookings/all-bookings");
    return response;
  } catch (error) {
    throw new Error(`Error fetching bookings: ${error.message}`);
  }
}

// This function gets booking by the confirmation code
export async function getBookingByConfirmationCode(confirmationCode) {
  try {
    const response = await api.get(`/api/bookings/confirmation/${confirmationCode}`);
    return response;
  } catch (error) {
    if (error.response && error.response.data) {
      throw new Error(error.response.data);
    } else {
      throw new Error(`Error find booking: ${error.message}`);
    }
  }
}

// This function cancels booking
export async function cancelBooking(bookingId) {
  try {
    const response = await api.delete(`/api/bookings/booking/${bookingId}/delete`);
    return response;
  } catch (error) {
    throw new Error(`Error cancelling booking: ${error.message}`);
  }
}

// This function gets all available rooms from the database with a given date and a room type
export async function getAvailableRooms(checkInDate, checkOutDate, roomType) { 
  const response = await api.get(`/api/rooms/available-rooms?checkInDate=${checkInDate}&checkOutDate=${checkOutDate}&roomType=${roomType}`);
  return response;
}

// This function register a new user
export async function registerUser(registration) {
  try {
    const response = await api.post("/api/auth/register-user", registration);
    return response;
  } catch (error) {
    if (error.response && error.response.data) {
      throw new Error(error.response.data);
    } else {
      throw new Error(`User registration error: ${error.message}`);
    }
  }
}

// This function login a registered user
export async function loginUser(login) {
  try {
    const response = await api.post("/api/auth/login", login);
    if (response.status >= 200 && response.status < 300) {
      return response;
    } else {
      return null;
    }
  } catch (error) {
    console.error(error);
    return null;
  }
}

// This is function to get the user profile
export async function getUserProfile(userId, token) {
  try {
    const response = await api.get(`/api/users/profile/${userId}`, {
      headers: getHeader()
    });
    return response;
  } catch (error) {
    return error.message;
  }
}

// This is the function to delete a user
export async function deleteUser(userId) {
  try {
    const response = await api.delete(`/api/users/delete/${userId}`, {
      headers: getHeader()
    });
    return response;
  } catch (error) {
    return error.message;
  }
}

// This is the function to get a single user
export async function getUser(userId, token) {
  try {
    const response = await api.get(`/api/users/${userId}`, {
      headers: getHeader()
    });
    return response;
  } catch (error) {
      return error.message;
  }
}

// This is the function to get user bookings by the user id
export async function getBookingsByUserId(userId, token) {
  try {
    const response = await api.get(`/api/bookings/user/${userId}/bookings`, {
      headers: getHeader()
    });
    return response;
  } catch (error) {
    console.error('Error fetching bookings: ', error.message);
    throw new Error('Failed to fetch bookings');
  }
}