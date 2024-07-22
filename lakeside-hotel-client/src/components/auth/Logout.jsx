import { useAuth } from './AuthProvider';
import { Link, useNavigate } from 'react-router-dom';

const Logout = () => {

    const navigate = useNavigate();

    const auth = useAuth();

    const handleLogout = () => {
        auth.handleLogout();
        navigate('/', {state: {message: 'You have been logged out!'}});
    }

    const isLoggedIn = auth.user !== null;

  return isLoggedIn ? (
    <>
        <li>
            <Link to={'/profile'} className='dropdown-item'>Profile</Link>
        </li>
        <li>
            <hr className='dropdown-divider' />
        </li>
        <button className='dropdown-item' onClick={handleLogout}>Logout</button>
    </>
  ) : null
}

export default Logout