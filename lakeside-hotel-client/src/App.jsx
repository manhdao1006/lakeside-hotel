import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
import '/node_modules/bootstrap/dist/js/bootstrap.min.js';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import ExistingRooms from './components/room/ExistingRooms';
import Home from './components/home/Home';
import EditRoom from './components/room/EditRoom';
import AddRoom from './components/room/AddRoom';

function App() {

  return (
    <>
    <main>
      <Router>
        <Routes>
          <Route path='/' element={ <Home /> } />
          <Route path='/add-room' element={ <AddRoom /> } />
          <Route path='/edit-room/:roomId' element={ <EditRoom /> } />
          <Route path='/existing-rooms' element={ <ExistingRooms /> } />
        </Routes>
      </Router>
    </main>
    </>
  )
}

export default App