package project.healthbox.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.healthbox.domain.entities.User;
import project.healthbox.domain.models.binding.UserLoginBindingModel;
import project.healthbox.domain.models.service.UserServiceModel;
import project.healthbox.repostory.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(ModelMapper modelMapper, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @Override
    public UserServiceModel register(UserServiceModel userServiceModel) {
        User user = this.modelMapper.map(userServiceModel, User.class);
        user.setPassword(DigestUtils.sha256Hex(user.getPassword()));
        return this.modelMapper.map(this.userRepository.saveAndFlush(user), UserServiceModel.class);
    }

    @Override
    public UserLoginBindingModel login(UserLoginBindingModel userLoginBindingModel) throws Exception {
        String hashedPassword = DigestUtils.sha256Hex(userLoginBindingModel.getPassword());
        User user = this.userRepository.findByEmailAndPassword(userLoginBindingModel.getEmail(), hashedPassword).orElseThrow(() -> new Exception("Invalid user"));
        return this.modelMapper.map(user, UserLoginBindingModel.class);
    }
}
