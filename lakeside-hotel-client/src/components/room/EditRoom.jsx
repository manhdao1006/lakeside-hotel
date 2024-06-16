import { useEffect, useState } from 'react';
import { getRoomById, updateRoom } from '../utils/ApiFunctions';
import { Link, useParams } from 'react-router-dom';

const EditRoom = () => {

    const {roomId} = useParams();

    const [room, setRoom] = useState({
      photo: null,
      roomType: '',
      roomPrice: null
    });

    const [successMessage, setSuccessMessage] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [imagePreview, setImagePreview] = useState('');

    const handleImageChange = (e) => {
      const selectedImage = e.target.files[0];
      setRoom({ ...room, photo: selectedImage });
      setImagePreview(URL.createObjectURL(selectedImage));
    }

    const handleInputChange = (e) => {
      const { name, value } = e.target;
      setRoom({ ...room, [name]: value });
    }

    useEffect(() => {
      const fetchRoom = async () => {
        try {
          const roomData = await getRoomById(roomId);
          setRoom(roomData.data);
          setImagePreview(roomData.data.photo);
        } catch (error) {
          console.error(error);
        }
      }

      fetchRoom();
    }, [roomId]);

    const handleSubmit = async (e) => {
      e.preventDefault();

      try {
        const response = await updateRoom(roomId, room);
        if(response.status === 200) {
          setSuccessMessage('Room updated successfully!');
          const updatedRoomData = await getRoomById(roomId);
          setRoom(updatedRoomData.data);
          setImagePreview(updatedRoomData.data.photo);
          setErrorMessage('');
        } else {
          setErrorMessage('Error updating room');
        }
      } catch (error) {
        console.error(error);
        setErrorMessage(error.message);
      }
    }

  return (
    <>
    <section className='container mt-5 mb-5'>
      <div className='row justify-content-center'>

        <div className='col-md-8 col-lg-6'>
          <h2 className='mt-5 mb-2'>Add a New Room</h2>
          {successMessage && (
            <div className='alert alert-success fade show'>{successMessage}</div>
          )}

          {errorMessage && (
            <div className='alert alert-danger fade show'>{errorMessage}</div>
          )}

          <form onSubmit={handleSubmit}>
            <div className='mb-3'>
              <label htmlFor='roomType' className='form-label hotel-color'>Room Type</label>
              <input
                type="text"
                className='form-control'
                id='roomType'
                name='roomType'
                value={room.roomType}
                onChange={handleInputChange}
              />
            </div>

            <div className='mb-3'>
              <label htmlFor='roomPrice' className='form-label hotel-color'>Room Price</label>
              <input
                className='form-control'
                id='roomPrice'
                type='number'
                name='roomPrice'
                value={room.roomPrice}
                onChange={handleInputChange}
              />
            </div>

            <div className='mb-3'>
              <label htmlFor='photo' className='form-label hotel-color'>Room Photo</label>
              <input
                className='form-control'
                id='photo'
                name='photo'
                type='file'
                onChange={handleImageChange}
              />
              {imagePreview && (
                <img
                  className='mt-3'
                  src={`data:image/jpeg;base64,${imagePreview}`} 
                  alt='Preview Room Photo' 
                  style={{ maxWidth: '400px', maxHeight: '400px' }}
                />
              )}

            </div>

            <div className='d-grid gap-2 d-md-flex mt-2'>
              <Link to={'/existing-rooms'} className='btn btn-outline-info ml-5'>Back</Link>
              <button type='submit' className='btn btn-outline-warning'>Edit Room</button>
            </div>

          </form>
        </div>

      </div>
    </section>      
    </>
  )
}

export default EditRoom
