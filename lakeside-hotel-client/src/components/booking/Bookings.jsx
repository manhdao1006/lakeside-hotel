import { useEffect, useState } from 'react';
import { cancelBooking, getAllBookings } from '../utils/ApiFunctions';
import Header from '../common/Header';
import BookingsTable from './BookingsTable';

const Bookings = () => {
  
    const [bookingInfo, setBookingInfo] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState('');
    
    useEffect(() => {
      setTimeout(() => {
        getAllBookings().then((response) => {
          setBookingInfo(response.data);
          setIsLoading(false);
        }).catch((error) => {
          setError(error.message);
          setIsLoading(false);
        })
      }, 3000);
    }, []);

    const handleBookingCancellation = async(bookingId) => {
      try {
        await cancelBooking(bookingId);
        const response = await getAllBookings();
        setBookingInfo(response.data);
      } catch (error) {
        setError(error.message);
      }
    }

  return (
    <section style={{backgroundColor: 'whitesmoke'}}>
      <Header title={'Existing Bookings'} />
      {error && <div className='text-danger'>{error}</div>}
      {isLoading ? (
        <div>Loading existing bookings ...</div>
      ) : (
        <BookingsTable bookingInfo={bookingInfo} handleBookingCancellation={handleBookingCancellation} />
      )}
    </section>
  )
}

export default Bookings