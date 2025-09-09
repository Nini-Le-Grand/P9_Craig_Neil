import { useState, useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { clearErrors } from "../../store/slices/profileSlice";
import { updateProfileThunk } from "../../store/services/profileService";
import { useNavigate } from "react-router-dom";
import Form from "../../components/forms/Form";
import profileFields from "../../components/forms/datas/profileFields";
import Title from "../../components/Title";
import AlertMessage from "../../components/AlertMessage";
import Container from "../../components/layouts/Container";
import { profileUrl } from "../../router/urls";

const ProfileUpdate = () => {
  const profile = useSelector((state) => state.profile);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const [values, setValues] = useState({
    firstName: profile.firstName,
    lastName: profile.lastName,
    email: profile.email,
    gender: profile.gender,
    dateOfBirth: profile.dateOfBirth,
    address: profile.address,
    phone: profile.phone,
  });

    useEffect(() => {
    if (profile?.id) {
      setValues({ ...profile });
    }
  }, [profile]);

  useEffect(() => {
    return () => {
      dispatch(clearErrors());
    };
  }, [dispatch]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setValues((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const result = await dispatch(updateProfileThunk(values));
    if (updateProfileThunk.fulfilled.match(result)) {
      navigate(profileUrl, {
        state: { success: "Profil mis à jour avec succès !" },
      });
    }
  };

  return (
    <Container>
      <Title title="Modifier mon profil" type="h2" />

      {profile.error && <AlertMessage message={profile.error} type="error" />}

      <Form
        fields={profileFields}
        values={values}
        fieldErrors={profile.fieldErrors}
        loading={profile.loading}
        onChange={handleChange}
        onSubmit={handleSubmit}
        onCancel={() => navigate(profileUrl)}
        submitLabel="Modifier"
        cancelLabel="Annuler"
      />
    </Container>
  );
};

export default ProfileUpdate;
