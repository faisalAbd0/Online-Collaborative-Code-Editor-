import LoginForm from "../auth_components/loginForm";
export default function LoginPage({ children }) {
    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">

            <LoginForm />
        </div>

    );
}