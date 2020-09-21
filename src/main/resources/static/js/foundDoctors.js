const URLS = {
    foundDoctors: '/user/foundDoctors',
};

const toString = ({ id, firstName, lastName, locationName}) => {
    let row = `
    <div class="col-lg-6">
                        <div class="member d-flex align-items-start">
                            <div class="pic"><img src="assets/img/doctors/doctors-1.jpg" class="img-fluid" alt=""></div>
                            <div class="member-info">
                                <h4>${firstName} ${lastName}</h4>
                                <span>${locationName}</span>
                                <a href="/doctor/profile/${id}" class="text-center btn btn-info text-white mb-3" data-animation="fadeInLeft" data-delay="0.5s">View Profile</a>
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


