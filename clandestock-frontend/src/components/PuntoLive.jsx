export const PuntoLive = () => {
    return (
        <>
            <span
                className="rounded-circle bg-success position-absolute"
                style={{
                    width: "10px",
                    height: "10px",
                    top: "10px",
                    right: "10px",
                    animation: "blink 1s infinite"
                }}
            ></span>
            <style>
                {`
                    @keyframes blink {
                    0% { opacity: 1; }
                    50% { opacity: 0; }
                    100% { opacity: 1; }
                    }
                `}
            </style>
        </>
    )
}
