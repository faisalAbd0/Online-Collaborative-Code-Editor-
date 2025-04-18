export default function ProfileEditForm({ formData, onChange, onSave }) {
    return (
        <div>
            <input
                type="text"
                name="firstName"
                value={formData.firstName}
                onChange={onChange}
                className="border p-2 rounded w-full mb-2"
            />
            <input
                type="text"
                name="lastName"
                value={formData.lastName}
                onChange={onChange}
                className="border p-2 rounded w-full mb-2"
            />
            <input
                type="text"
                name="userIdentifier"
                value={formData.userIdentifier}
                onChange={onChange}
                className="border p-2 rounded w-full mb-2"
            />
            <button
                onClick={onSave}
                className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600">
                Save
            </button>
        </div>
    );
}
