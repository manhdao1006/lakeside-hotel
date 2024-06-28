import HotelService from '../common/HotelService';
import Parallax from '../common/Parallax';
import RoomCarousel from '../common/RoomCarousel';
import RoomSearch from '../common/RoomSearch';
import MainHeader from '../layout/MainHeader';

const Home = () => {
  return (
    <section>
      <MainHeader />
      <section className='container'>
        <RoomSearch />
        <RoomCarousel />
        <Parallax />
        <RoomCarousel />
        <HotelService />
        <Parallax />
        <RoomCarousel />
      </section>
    </section>
  )
}

export default Home
