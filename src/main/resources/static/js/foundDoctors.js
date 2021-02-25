const URLS = {
    foundDoctors: '/user/foundDoctors',
};

const toString = ({ id, firstName, lastName, locationName, imageUrl, specialtyName}) => {
    let row = `
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
                                <!---<a href="/doctor/profile/${id}" class="text-center btn btn-info text-white mb-3" data-animation="fadeInLeft" data-delay="0.5s">View Profile</a>--!>
                            </div>
                            </div>
                        </div>
                    </div>
`
    return `<div class="row">${row}</div>`
};

fetch(URLS.foundDoctors)
    .then(response => response.json())
    .then(items => {
        let result = '';
        items.forEach(item => {
            const itemString = toString(item);
            result += itemString;
        });
        $('#foundDoctors').html(result);
    });


