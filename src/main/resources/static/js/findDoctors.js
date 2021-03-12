const toString = ({id, firstName, lastName, locationName, imageUrl, specialtyName}) => {
    return `
    <div class="col-lg-6">
                        <div class="member d-flex align-items-start">
                            <div class="pic"><img src="${imageUrl}" class="img-fluid" alt="Doctor Profile Picture"></div>
                            <div class="member-info">
                                <h4>${firstName} ${lastName}</h4>
                                <br/>
                                <span>City: ${locationName}</span>
                                <br/>
                                <span>Specialty: ${specialtyName}</span>
                                <br/>
                                <a href="/doctor/profile/${id}" class="text-center btn btn-info text-white mb-3" data-animation="fadeInLeft" data-delay="0.5s">View Profile</a>
                            </div>
                            </div>
                        </div>
                    </div>
`;
};

$('#loadDoctors').click(() => {
    const specialtyId = document.getElementById('specialty').value;
    const city_id = document.getElementById('location').value;
    const doctorName = document.getElementById('doctorName').value;

    let firstName;
    let lastName;

    if (doctorName && doctorName !== "" && doctorName.includes(" ")) {
        firstName = document.getElementById('doctorName').value.split(' ')[0];
        lastName = document.getElementById('doctorName').value.split(' ')[1];
    } else {
        firstName = "";
        lastName = "";
    }

    fetch('http://localhost:8000/findDoctors/specialty_id=' + specialtyId + '&city_id=' + city_id + '&first_name=' + firstName + '&last_name=' + lastName)
        .then((response) => {
            if (response.status === 404) {
                $('#doctors').attr('hidden', 'hidden');
                $('#info').html('' +
                    '<h2>No Doctors Found!</h2>\n' +
                    '<p>Try different search criteria!</p>'
                )
            }
            return response.json();
        })
        .then(items => {
            if (Object.keys(items).length > 0) {
                $('#doctors').removeAttr('hidden');
                let result = '';
                items.forEach(item => {
                    const itemString = toString(item);
                    result += itemString;
                });
                $('#showDoctors').html(result);
                $('#info').html('' +
                    '<h2>Choose from our specialists.</h2>\n' +
                    '<p>Find the one who you need.</p>'
                )
            }
        });
});


