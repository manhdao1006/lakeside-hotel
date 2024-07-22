import { useState } from 'react';
import { registerUser } from '../utils/ApiFunctions';
import { Link } from 'react-router-dom';

const Registration = () => {

    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const [registration, setRegistration] = useState({
        firstName: '',
        lastName: '',
        email: '',
        password: '',
    });

    const handleInputChange = (e) => {
        setRegistration({ ... registration, [e.target.name] : e.target.value });
    }

    const handleRegistration = async(e) => {
        e.preventDefault();
        try {
            const response = await registerUser(registration);
            setSuccessMessage(response.data);
            setErrorMessage('');
            setRegistration({ firstName: '',
                                lastName: '',
                                email: '',
                                password: '', })
        } catch (error) {
            setSuccessMessage('');
            setErrorMessage(`Registration error: ${error.message}`);
        }

        setTimeout(() => {
            setErrorMessage('');
            setSuccessMessage('');
        }, 3000);
    }

  return (
    <section className='container col-6 mt-5 mb-5'>
        {errorMessage && <p className='alert alert-danger'>{errorMessage}</p>}
        {successMessage && <p className='alert alert-success'>{successMessage}</p>}

        <h2>Register</h2>
        <form onSubmit={handleRegistration}>
            <div className='row mb-3'>
                <label htmlFor="firstName" className='col-sm-2 col-form-label'>First Name</label>
                <div className='col-sm-10'>
                    <input
                        type="text"
                        name="firstName"
                        id="firstName"
                        className='form-control'
                        value={registration.firstName}
                        onChange={handleInputChange}
                        placeholder='Enter your first name'
                    />
                </div>
            </div>

            <div className='row mb-3'>
                <label htmlFor="lastName" className='col-sm-2 col-form-label'>Last Name</label>
                <div className='col-sm-10'>
                    <input
                        type="text"
                        name="lastName"
                        id="lastName"
                        className='form-control'
                        value={registration.lastName}
                        onChange={handleInputChange}
                        placeholder='Enter your last name'
                    />
                </div>
            </div>

            <div className='row mb-3'>
                <label htmlFor="email" className='col-sm-2 col-form-label'>Email</label>
                <div className='col-sm-10'>
                    <input
                        type="email"
                        name="email"
                        id="email"
                        className='form-control'
                        value={registration.email}
                        onChange={handleInputChange}
                        placeholder='Enter your email'
                    />
                </div>
            </div>

            <div className='row mb-3'>
                <label htmlFor="password" className='col-sm-2 col-form-label'>Password</label>
                <div className='col-sm-10'>
                    <input
                        type="password"
                        name="password"
                        id="password"
                        className='form-control'
                        value={registration.password}
                        onChange={handleInputChange}
                        placeholder='Enter your password'
                    />
                </div>
            </div>

            <div className='mb-3'>
                <button
                    type='submit'
                    className='btn btn-hotel'
                    style={{marginRight: '10px'}}
                >
                        Register
                </button>
                <span style={{marginLeft: '10px'}}>
                    Already have an account? <Link to={'/login'}>Login</Link>
                </span>
            </div>
        </form>
    </section>
  )
}

export default Registration