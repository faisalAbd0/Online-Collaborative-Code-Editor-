export default function ProfileInfo({ user, onEdit }) {
    return (
        <div>
            <p className="text-lg"><strong>First Name:</strong> {user.firstName}</p>
            <p className="text-lg"><strong>Last Name:</strong> {user.lastName}</p>
            <p className="text-lg"><strong>Username:</strong> {user.userIdentifier}</p>
            <p className="text-lg"><strong>Email:</strong> {user.email}</p>

            <button
                onClick={onEdit}
                className="bg-blue-500 text-white px-4 py-2 mt-4 rounded hover:bg-blue-600">
                Edit
            </button>
        </div>
    );
}
