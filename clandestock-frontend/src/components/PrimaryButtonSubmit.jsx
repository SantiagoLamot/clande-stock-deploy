const PrimaryButtonSubmit = ({ label }) => {
    return (
        <div className="text-center">
            <button
                className="btn btn-warning fw-bold px-4 py-2 border-dark"
                type="submit"
            >
                {label}
            </button>
        </div>
    );
};

export default PrimaryButtonSubmit;