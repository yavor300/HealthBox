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
    $('#doctors').removeAttr('hidden');

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
        .then((response) => response.json())
        .then(items => {

            if (Object.keys(items).length === 0) {
                $('#nothingFound').text('No results');
                console.log("No Res.");
            } else {
                let result = '';
                items.forEach(item => {
                    const itemString = toString(item);
                    result += itemString;
                });
                $('#showDoctors').html(result);
            }
        });
});


