import axios from "axios";

export const api = axios.create({
  baseURL : "http://localhost:9192"
})

// This function add room to the thee database
export async function addRoom(photo, roomType, roomPrice) {
  const formData = new FormData();
  formData.append("photo", photo);
  formData.append("roomType", roomType);
  formData.append("roomPrice", roomPrice);

  const response = await api.post("/api/rooms/add/new-room", formData);
  if(response.status === 201){
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
export async function getAllRooms(){
  try {
    const response = await api.get("/api/rooms/all-rooms");
    return response;
  } catch (error) {
    throw new Error("Error fetching rooms");
  }
}

// This function deletes a room by Id
export async function deleteRoom(roomId){
  try {
    const response = await api.delete(`/api/rooms/delete/room/${roomId}`);
    return response;
  } catch (error) {
    throw new Error(`Error deleting room ${error.message}`);
  }
}